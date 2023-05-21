package com.pamparamm.habittracker.presentation.viewmodel.habitslist

import com.pamparamm.habittracker.domain.entities.HabitPriority

data class HabitsListSearchQuery(
    val name: String?,
    val priority: HabitPriority?,
    val isCompleted: Boolean?
)