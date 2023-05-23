package com.pamparamm.habittracker.domain.repository

import com.pamparamm.habittracker.domain.entities.Habit
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface HabitsRepository {
    fun getHabits(): Flow<List<Habit>>
    suspend fun getHabit(uuid: UUID): Habit?

    suspend fun createHabit(habit: Habit)
    suspend fun updateHabit(habit: Habit)
    suspend fun deleteHabit(uuid: UUID)
    suspend fun completeHabit(uuid: UUID, timestamp: Long)

    suspend fun syncWithRemote()
}