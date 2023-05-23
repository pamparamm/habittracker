package com.pamparamm.habittracker.data.store.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.pamparamm.habittracker.data.store.local.dto.HabitCompletionConverter
import com.pamparamm.habittracker.data.store.local.dto.HabitLocalDto

@Database(entities = [HabitLocalDto::class], version = 1, exportSchema = false)
@TypeConverters(HabitCompletionConverter::class)
abstract class RoomDataStore : RoomDatabase() {
    abstract fun habitsDao(): HabitsDao
}