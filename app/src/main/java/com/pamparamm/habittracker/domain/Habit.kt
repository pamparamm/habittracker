package com.pamparamm.habittracker.domain

data class Habit(
    val title: String = "Habit Title",
    val description: String = "Description",
    val priority: HabitPriority = HabitPriority.HIGH,
    val type: HabitType = HabitType.GOOD,
    val frequency: Int = 1,
    val repeats: Int = 1
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