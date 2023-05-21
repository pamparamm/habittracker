package com.pamparamm.habittracker.presentation.viewmodel.habitslist.store

import com.pamparamm.habittracker.domain.usecases.HabitsUseCase
import com.pamparamm.habittracker.presentation.viewmodel.store.Middleware
import com.pamparamm.habittracker.presentation.viewmodel.store.Store
import kotlinx.coroutines.flow.distinctUntilChanged

class HabitsListUseCaseMiddleware(
    private val habitsUseCase: HabitsUseCase
) : Middleware<HabitsListState, HabitsListAction> {
    override suspend fun invoke(
        state: HabitsListState,
        action: HabitsListAction,
        store: Store<HabitsListState, HabitsListAction>,
    ) {
        when (action) {
            HabitsListAction.ObserveHabits ->
                habitsUseCase.getHabits().distinctUntilChanged().collect {
                    val getHabits = HabitsListAction.GetHabits(it)
                    store.dispatch(getHabits)
                }

            is HabitsListAction.DeleteSelectedHabit -> {
                state.selectedHabit?.let { habitsUseCase.deleteHabit(it.id) }
                store.dispatch(HabitsListAction.DeselectHabit)
            }

            is HabitsListAction.CompleteSelectedHabit ->
                state.selectedHabit?.let { habitsUseCase.completeHabit(it.id) }

            else -> {}
        }
    }
}