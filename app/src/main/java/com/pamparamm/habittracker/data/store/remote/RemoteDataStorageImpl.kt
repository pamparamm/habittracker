package com.pamparamm.habittracker.data.store.remote

import com.pamparamm.habittracker.data.store.remote.dto.HabitDoneDto
import com.pamparamm.habittracker.data.store.remote.dto.HabitRemoteDto
import com.pamparamm.habittracker.data.store.remote.dto.HabitUidDto
import javax.inject.Inject

class RemoteDataStorageImpl @Inject constructor(private val remoteDataApi: RemoteDataApi) :
    RemoteDataStorage {
    override suspend fun getHabits(): List<HabitRemoteDto> = remoteDataApi.getHabits()

    override suspend fun addHabit(habitRemoteDto: HabitRemoteDto): HabitUidDto =
        remoteDataApi.putHabit(habitRemoteDto.copy(uid = null))

    override suspend fun updateHabit(habitRemoteDto: HabitRemoteDto): HabitUidDto =
        remoteDataApi.putHabit(habitRemoteDto)

    override suspend fun deleteHabit(habitUidDto: HabitUidDto) =
        remoteDataApi.deleteHabit(habitUidDto)

    override suspend fun completeHabit(habitDoneDto: HabitDoneDto) =
        remoteDataApi.completeHabit(habitDoneDto)
}