package com.pamparamm.habittracker.presentation.viewmodel.habitslist.store

import com.pamparamm.habittracker.domain.usecases.HabitsUseCase
import com.pamparamm.habittracker.presentation.viewmodel.store.Middleware
import com.pamparamm.habittracker.presentation.viewmodel.store.Store
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class HabitsListUseCaseMiddleware(
    private val habitsUseCase: HabitsUseCase,
    private val scope: CoroutineScope
) : Middleware<HabitsListState, HabitsListAction> {
    override suspend fun invoke(
        state: HabitsListState,
        action: HabitsListAction,
        store: Store<HabitsListState, HabitsListAction>
    ) {
        when (action) {
            HabitsListAction.ObserveHabits -> scope.launch {
                habitsUseCase.getHabits().distinctUntilChanged().collect {
                    store.dispatch(HabitsListAction.GetHabits(it))
                }
            }

            is HabitsListAction.DeleteSelectedHabit -> {
                state.selectedHabit?.let { habitsUseCase.deleteHabit(it.id) }
                store.dispatch(HabitsListAction.DeselectHabit)
            }

            is HabitsListAction.CompleteSelectedHabit ->
                state.selectedHabit?.let {
                    habitsUseCase.completeHabit(it.id)
                    store.dispatch(HabitsListAction.PushCompletionMessage(it))
                }

            HabitsListAction.SyncWithRemote ->
                habitsUseCase.syncWithRemote()

            else -> {}
        }
    }
}