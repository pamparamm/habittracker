package com.pamparamm.habittracker.presentation.viewmodel.habitseditor.store

import com.pamparamm.habittracker.domain.entities.Habit
import com.pamparamm.habittracker.presentation.viewmodel.store.Reducer

class HabitsEditorReducer : Reducer<HabitsEditorState, HabitsEditorAction> {
    override fun invoke(state: HabitsEditorState, action: HabitsEditorAction): HabitsEditorState =
        when (action) {
            HabitsEditorAction.CreateHabit -> state.copy(
                isNewHabit = true,
                habit = Habit.empty()
            )

            is HabitsEditorAction.EditHabit -> state.copy(
                isNewHabit = false,
                habit = action.habit
            )

            is HabitsEditorAction.UpdateHabitData -> state.habit?.let {
                state.copy(
                    habit = action.habitData.modifyHabit(
                        it
                    )
                )
            } ?: state

            HabitsEditorAction.Cancel -> state.copy(isDoneEditing = true)

            else -> state
        }
}