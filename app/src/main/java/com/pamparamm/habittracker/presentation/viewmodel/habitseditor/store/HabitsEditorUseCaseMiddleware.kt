package com.pamparamm.habittracker.presentation.viewmodel.habitseditor.store

import com.pamparamm.habittracker.domain.usecases.HabitsUseCase
import com.pamparamm.habittracker.presentation.viewmodel.store.Middleware
import com.pamparamm.habittracker.presentation.viewmodel.store.Store

class HabitsEditorUseCaseMiddleware(
    private val habitsUseCase: HabitsUseCase
) : Middleware<HabitsEditorState, HabitsEditorAction> {
    override suspend fun invoke(
        state: HabitsEditorState,
        action: HabitsEditorAction,
        store: Store<HabitsEditorState, HabitsEditorAction>
    ) {
        when (action) {
            is HabitsEditorAction.RetrieveHabit -> action.habitUUID?.let {
                habitsUseCase.getHabit(it)?.let { habit ->
                    store.dispatch(HabitsEditorAction.EditHabit(habit))
                } ?: store.dispatch(HabitsEditorAction.CreateHabit)
            } ?: store.dispatch(HabitsEditorAction.CreateHabit)

            HabitsEditorAction.SaveHabit -> state.habit?.let {
                if (state.isNewHabit) habitsUseCase.createHabit(it)
                else habitsUseCase.updateHabit(it)
                store.dispatch(HabitsEditorAction.Cancel)
            }

            else -> {}
        }
    }
}