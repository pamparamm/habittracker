package com.pamparamm.habittracker.domain

import java.util.UUID

data class Habit(
    val id: UUID = UUID.randomUUID(),
    val title: String = "Habit Title",
    val description: String = "Description",
    val priority: HabitPriority = HabitPriority.HIGH,
    val type: HabitType = HabitType.GOOD,
    val frequency: Int = 1,
    val repeats: Int = 1,
    val creationTime: Long = System.currentTimeMillis()
)

enum class HabitPriority {
    HIGH,
    MEDIUM,
    LOW
}

enum class HabitType {
    GOOD,
    BAD
}