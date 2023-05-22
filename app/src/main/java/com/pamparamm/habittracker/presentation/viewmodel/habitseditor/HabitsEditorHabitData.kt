package com.pamparamm.habittracker.presentation.viewmodel.habitseditor

import com.pamparamm.habittracker.domain.entities.Habit
import com.pamparamm.habittracker.domain.entities.HabitPriority
import com.pamparamm.habittracker.domain.entities.HabitType

data class HabitsEditorHabitData(
    val title: String,
    val description: String,
    val priority: HabitPriority,
    val type: HabitType,
    val period: Int,
    val targetCompletions: Int
) {
    fun modifyHabit(habit: Habit): Habit = habit.copy(
        title = title,
        description = description,
        priority = priority,
        type = type,
        period = period,
        targetCompletions = targetCompletions
    )

    companion object {
        fun fromHabit(habit: Habit): HabitsEditorHabitData = HabitsEditorHabitData(
            habit.title,
            habit.description,
            habit.priority,
            habit.type,
            habit.period,
            habit.targetCompletions
        )
    }
}