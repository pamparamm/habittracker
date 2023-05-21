package com.pamparamm.habittracker.domain.entities

enum class HabitPriority {
    LOW, MEDIUM, HIGH;

    companion object {
        operator fun invoke(int: Int): HabitPriority = values()[int]
    }
}