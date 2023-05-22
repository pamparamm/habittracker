package com.pamparamm.habittracker.domain.usecases

import com.pamparamm.habittracker.domain.entities.Habit
import com.pamparamm.habittracker.domain.repository.HabitsRepository
import com.pamparamm.habittracker.domain.services.TimestampService
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import javax.inject.Inject

class HabitsUseCaseImpl @Inject constructor(
    private val habitsRepository: HabitsRepository,
    private val timestampService: TimestampService
) :
    HabitsUseCase {
    override suspend fun getHabits(): Flow<List<Habit>> = habitsRepository.getHabits()
    override suspend fun getHabit(uuid: UUID): Habit? = habitsRepository.getHabit(uuid)

    override suspend fun createHabit(habit: Habit): Habit {
        val habitToWrite = habit.copy(timestamp = timestampService.now())
        habitsRepository.createHabit(habitToWrite)
        return habitToWrite
    }

    override suspend fun updateHabit(habit: Habit) =
        habitsRepository.updateHabit(habit.copy(timestamp = timestampService.now()))

    override suspend fun deleteHabit(uuid: UUID) = habitsRepository.deleteHabit(uuid)
    override suspend fun completeHabit(uuid: UUID) =
        habitsRepository.completeHabit(uuid, timestampService.now())

    override suspend fun syncWithRemote() = habitsRepository.syncWithRemote()
}