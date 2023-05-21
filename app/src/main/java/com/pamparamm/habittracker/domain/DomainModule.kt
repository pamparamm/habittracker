package com.pamparamm.habittracker.domain

import com.pamparamm.habittracker.domain.services.TimestampService
import com.pamparamm.habittracker.domain.services.TimestampServiceImpl
import com.pamparamm.habittracker.domain.usecases.HabitsUseCase
import com.pamparamm.habittracker.domain.usecases.HabitsUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DomainModule {
    @Binds
    abstract fun bindTimestampService(timestampServiceImpl: TimestampServiceImpl): TimestampService

    @Binds
    abstract fun bindHabitsUseCase(habitsUseCaseImpl: HabitsUseCaseImpl): HabitsUseCase
}