package com.pamparamm.habittracker.domain.entities

enum class HabitType {
    GOOD, BAD;

    companion object {
        operator fun invoke(int: Int): HabitType = values()[int]
    }
}