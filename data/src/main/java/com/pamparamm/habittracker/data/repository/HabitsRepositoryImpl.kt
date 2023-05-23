package com.pamparamm.habittracker.data.repository

import com.pamparamm.habittracker.data.store.local.HabitsDao
import com.pamparamm.habittracker.data.store.local.dto.HabitLocalDto
import com.pamparamm.habittracker.data.store.remote.RemoteDataStorage
import com.pamparamm.habittracker.data.store.remote.dto.HabitDoneDto
import com.pamparamm.habittracker.data.store.remote.dto.HabitRemoteDto
import com.pamparamm.habittracker.data.store.remote.dto.HabitUidDto
import com.pamparamm.habittracker.domain.entities.Habit
import com.pamparamm.habittracker.domain.repository.HabitsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject

class HabitsRepositoryImpl @Inject constructor(
    private val habitsDao: HabitsDao,
    private val remoteDataStorage: RemoteDataStorage
) : HabitsRepository {
    override fun getHabits(): Flow<List<Habit>> =
        habitsDao.getHabits().map { habits -> habits.map { it.toHabit() } }

    override suspend fun getHabit(uuid: UUID): Habit? =
        habitsDao.getHabit(uuid)?.toHabit()

    override suspend fun createHabit(habit: Habit) {
        val uuid = UUID.fromString(remoteDataStorage.addHabit(HabitRemoteDto.fromHabit(habit)).uid)
        habitsDao.insertHabit(HabitLocalDto.fromHabit(habit.copy(id = uuid)))
    }

    override suspend fun updateHabit(habit: Habit) {
        remoteDataStorage.updateHabit(HabitRemoteDto.fromHabit(habit))
        habitsDao.updateHabit(HabitLocalDto.fromHabit(habit))
    }

    override suspend fun deleteHabit(uuid: UUID) {
        remoteDataStorage.deleteHabit(HabitUidDto(uuid.toString()))
        habitsDao.deleteHabit(uuid)
    }

    override suspend fun syncWithRemote() {
        habitsDao.deleteAllHabits()
        val habitsFromRemote =
            remoteDataStorage.getHabits().map { HabitLocalDto.fromHabit(it.toHabit()) }
        habitsDao.insertHabits(habitsFromRemote)
    }

    override suspend fun completeHabit(uuid: UUID, timestamp: Long) {
        remoteDataStorage.completeHabit(HabitDoneDto(uuid.toString(), timestamp))
        habitsDao.getHabit(uuid)?.let {
            habitsDao.updateHabit(it.copy(completions = it.completions.plus(timestamp)))
        } ?: Unit
    }
}