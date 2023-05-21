package com.pamparamm.habittracker.presentation.ui.extensions

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.pamparamm.habittracker.R
import com.pamparamm.habittracker.domain.entities.HabitPriority
import com.pamparamm.habittracker.domain.entities.HabitType

object Helpers {
    fun String.normalize(singleLine: Boolean = false): String =
        this.trimStart().also { if (singleLine) return this.trim('\n', '\r') }

    @Composable
    fun HabitType.nameResource(): String = when (this) {
        HabitType.GOOD -> stringResource(R.string.habit_type_good)
        HabitType.BAD -> stringResource(R.string.habit_type_bad)
    }

    @Composable
    fun HabitPriority.nameResource(): String = when (this) {
        HabitPriority.LOW -> stringResource(R.string.habit_priority_low)
        HabitPriority.MEDIUM -> stringResource(R.string.habit_priority_medium)
        HabitPriority.HIGH -> stringResource(R.string.habit_priority_high)
    }
}