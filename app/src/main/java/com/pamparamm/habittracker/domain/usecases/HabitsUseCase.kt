package com.pamparamm.habittracker.domain.usecases

import com.pamparamm.habittracker.domain.entities.Habit
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface HabitsUseCase {
    suspend fun getHabits(): Flow<List<Habit>>
    suspend fun getHabit(uuid: UUID): Habit?

    suspend fun createHabit(habit: Habit): Habit
    suspend fun updateHabit(habit: Habit)
    suspend fun deleteHabit(uuid: UUID)
    suspend fun completeHabit(uuid: UUID)

    suspend fun syncWithRemote()
}