package com.pamparamm.habittracker.data.store.local.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pamparamm.habittracker.domain.entities.Habit
import com.pamparamm.habittracker.domain.entities.HabitPriority
import com.pamparamm.habittracker.domain.entities.HabitType
import java.util.UUID

@Entity(tableName = "habits")
data class HabitLocalDto(
    @PrimaryKey
    @ColumnInfo(name = "uuid")
    val id: UUID,
    val title: String,
    val description: String,
    val priority: Int,
    val type: Int,
    val period: Int,
    val targetCompletions: Int,
    val completions: List<Long>,
    val timestamp: Long
) {
    fun toHabit(): Habit = Habit(
        id,
        title,
        description,
        HabitPriority(priority),
        HabitType(type),
        period,
        targetCompletions,
        completions,
        timestamp
    )

    companion object {
        fun fromHabit(src: Habit): HabitLocalDto =
            HabitLocalDto(
                src.id,
                src.title,
                src.description,
                src.priority.ordinal,
                src.type.ordinal,
                src.period,
                src.targetCompletions,
                src.completions,
                src.timestamp
            )
    }
}