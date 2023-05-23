package com.pamparamm.habittracker.data.store.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.pamparamm.habittracker.data.store.local.dto.HabitLocalDto
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface HabitsDao {
    @Query("SELECT * FROM habits")
    fun getHabits(): Flow<List<HabitLocalDto>>

    @Query("SELECT * FROM habits WHERE uuid == :uuid")
    suspend fun getHabit(uuid: UUID): HabitLocalDto?

    @Insert
    suspend fun insertHabit(habitLocalDto: HabitLocalDto)

    @Insert
    suspend fun insertHabits(habits: List<HabitLocalDto>)

    @Update
    suspend fun updateHabit(habitLocalDto: HabitLocalDto)

    @Query("DELETE FROM habits WHERE uuid = :uuid")
    suspend fun deleteHabit(uuid: UUID)

    @Query("DELETE FROM habits")
    suspend fun deleteAllHabits()
}