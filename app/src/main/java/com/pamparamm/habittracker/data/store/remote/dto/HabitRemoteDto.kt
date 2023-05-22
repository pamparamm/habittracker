package com.pamparamm.habittracker.data.store.remote.dto

import com.pamparamm.habittracker.domain.UUIDExtensions
import com.pamparamm.habittracker.domain.entities.Habit
import com.pamparamm.habittracker.domain.entities.HabitPriority
import com.pamparamm.habittracker.domain.entities.HabitType
import java.util.UUID

data class HabitRemoteDto(
    val uid: String?,
    val title: String,
    val description: String,
    val priority: Int,
    val type: Int,
    val frequency: Int,
    val count: Int,
    val doneDates: List<Long>,
    val date: Long
) {
    fun toHabit(): Habit = Habit(
        id = uid?.let { UUID.fromString(it) } ?: UUIDExtensions.emptyUUID(),
        title = title,
        description = description,
        priority = HabitPriority(priority),
        type = HabitType(type),
        period = frequency,
        targetCompletions = count,
        completions = doneDates,
        timestamp = date
    )

    companion object {
        fun fromHabit(src: Habit): HabitRemoteDto = HabitRemoteDto(
            uid = src.id.toString(),
            title = src.title,
            description = src.description,
            priority = src.priority.ordinal,
            type = src.type.ordinal,
            frequency = src.period,
            count = src.targetCompletions,
            doneDates = src.completions,
            date = src.timestamp
        )
    }
}