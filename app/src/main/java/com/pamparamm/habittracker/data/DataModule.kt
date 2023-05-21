package com.pamparamm.habittracker.data

import android.content.Context
import androidx.room.Room
import com.pamparamm.habittracker.data.repository.HabitsRepositoryImpl
import com.pamparamm.habittracker.data.store.local.RoomDataStore
import com.pamparamm.habittracker.data.store.local.HabitsDao
import com.pamparamm.habittracker.domain.repository.HabitsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds
    abstract fun bindHabitsRepository(habitsRepositoryImpl: HabitsRepositoryImpl): HabitsRepository

    companion object {
        @Provides
        fun provideHabitsDao(@ApplicationContext context: Context): HabitsDao =
            Room.databaseBuilder(context, RoomDataStore::class.java, "appdb").build().habitsDao()
    }
}