package com.pamparamm.habittracker.data.store.remote

import com.pamparamm.habittracker.data.store.remote.dto.HabitDoneDto
import com.pamparamm.habittracker.data.store.remote.dto.HabitRemoteDto
import com.pamparamm.habittracker.data.store.remote.dto.HabitUidDto

interface RemoteDataStorage {
    suspend fun getHabits(): List<HabitRemoteDto>
    suspend fun addHabit(habitRemoteDto: HabitRemoteDto): HabitUidDto
    suspend fun updateHabit(habitRemoteDto: HabitRemoteDto): HabitUidDto
    suspend fun deleteHabit(habitUidDto: HabitUidDto)
    suspend fun completeHabit(habitDoneDto: HabitDoneDto)
}