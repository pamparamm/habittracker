package com.pamparamm.habittracker.domain.entities

import com.pamparamm.habittracker.domain.UUIDExtensions
import java.util.UUID

data class Habit(
    val id: UUID,
    val title: String,
    val description: String,
    val priority: HabitPriority,
    val type: HabitType,
    val period: Int,
    val targetCompletions: Int,
    val completions: List<Long>,
    val timestamp: Long
) {
    companion object {
        fun empty(): Habit =
            Habit(
                UUIDExtensions.emptyUUID(),
                "Habit",
                "",
                HabitPriority.LOW,
                HabitType.GOOD,
                1,
                2,
                emptyList(),
                0
            )
    }
}