package com.pamparamm.habittracker.presentation.viewmodel.habitseditor.store

import com.pamparamm.habittracker.domain.entities.Habit
import com.pamparamm.habittracker.presentation.viewmodel.habitseditor.HabitsEditorHabitData
import com.pamparamm.habittracker.presentation.viewmodel.store.Action
import java.util.UUID

sealed class HabitsEditorAction : Action {
    data class InitHabitEditor(val habitUUID: UUID?) : HabitsEditorAction()
    object CreateHabit : HabitsEditorAction()
    data class EditHabit(val habit: Habit) : HabitsEditorAction()
    data class UpdateHabitData(val habitData: HabitsEditorHabitData) : HabitsEditorAction()
    object SaveHabit : HabitsEditorAction()
    object Cancel : HabitsEditorAction()
}