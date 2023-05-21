package com.pamparamm.habittracker.data.repository

import com.pamparamm.habittracker.data.store.local.HabitsDao
import com.pamparamm.habittracker.data.store.local.dto.HabitLocalDto
import com.pamparamm.habittracker.domain.entities.Habit
import com.pamparamm.habittracker.domain.repository.HabitsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject

class HabitsRepositoryImpl @Inject constructor(
    private val habitsDao: HabitsDao
) : HabitsRepository {
    override fun getHabits(): Flow<List<Habit>> =
        habitsDao.getHabits().map { habits -> habits.map { it.toHabit() } }

    override suspend fun getHabit(uuid: UUID): Habit? =
        habitsDao.getHabit(uuid)?.toHabit()

    override suspend fun createHabit(habit: Habit): Unit =
        habitsDao.insertHabit(HabitLocalDto.fromHabit(habit))

    override suspend fun updateHabit(habit: Habit) =
        habitsDao.updateHabit(HabitLocalDto.fromHabit(habit))

    override suspend fun deleteHabit(uuid: UUID) =
        habitsDao.deleteHabit(uuid)

    override suspend fun syncWithRemote() {
        TODO("Not yet implemented")
    }

    override suspend fun completeHabit(uuid: UUID, timestamp: Long) =
        habitsDao.getHabit(uuid)?.let {
            habitsDao.updateHabit(it.copy(completions = it.completions.plus(timestamp)))
        } ?: Unit
}