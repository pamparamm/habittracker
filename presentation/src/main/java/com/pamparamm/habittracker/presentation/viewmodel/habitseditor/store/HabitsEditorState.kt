package com.pamparamm.habittracker.presentation.viewmodel.habitseditor.store

import com.pamparamm.habittracker.domain.entities.Habit
import com.pamparamm.habittracker.presentation.viewmodel.store.State

data class HabitsEditorState(
    val isNewHabit: Boolean,
    val habit: Habit?,
    val isDoneEditing: Boolean
) : State {
    companion object {
        fun initialState(): HabitsEditorState = HabitsEditorState(
            isNewHabit = false,
            habit = null,
            isDoneEditing = false
        )
    }
}