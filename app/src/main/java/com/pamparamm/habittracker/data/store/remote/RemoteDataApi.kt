package com.pamparamm.habittracker.data.store.remote

import com.pamparamm.habittracker.data.store.remote.dto.HabitDoneDto
import com.pamparamm.habittracker.data.store.remote.dto.HabitRemoteDto
import com.pamparamm.habittracker.data.store.remote.dto.HabitUidDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.PUT

interface RemoteDataApi {
    @GET("habit")
    suspend fun getHabits(): List<HabitRemoteDto>

    @PUT("habit")
    suspend fun putHabit(@Body habitRemoteDto: HabitRemoteDto): HabitUidDto

    @HTTP(method = "DELETE", path = "habit", hasBody = true)
    suspend fun deleteHabit(@Body habitUidDto: HabitUidDto)

    @POST("habit_done")
    suspend fun completeHabit(@Body habitDoneDto: HabitDoneDto)
}