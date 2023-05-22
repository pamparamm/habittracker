package com.pamparamm.habittracker.presentation.viewmodel.habitslist

import com.pamparamm.habittracker.domain.entities.Habit
import com.pamparamm.habittracker.domain.entities.HabitPriority

data class HabitsListSearchQuery(
    val title: String?,
    val priority: HabitPriority?,
    val isCompleted: Boolean?
) {
    fun matchesHabit(habit: Habit) =
        title?.let { habit.title.lowercase().contains(it.lowercase()) } ?: true &&
                priority?.let { it == habit.priority } ?: true &&
                isCompleted?.let { it == habit.completions.count() >= habit.targetCompletions } ?: true

    companion object {
        fun empty(): HabitsListSearchQuery = HabitsListSearchQuery(
            title = null,
            priority = null,
            isCompleted = null
        )
    }
}