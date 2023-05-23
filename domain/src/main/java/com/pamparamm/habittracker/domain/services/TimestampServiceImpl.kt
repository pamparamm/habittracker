package com.pamparamm.habittracker.domain.services

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.Clock
import java.time.Instant
import javax.inject.Inject

class TimestampServiceImpl @Inject constructor() : TimestampService {
    private val secondsInPeriod = 24L * 60 * 60

    override fun now(): Long {
        val clock = Clock.systemUTC()
        val instant = Instant.now(clock)
        return instant.epochSecond
    }

    override fun periodsBetween(now: Long, then: Long): Int =
        (((now - then) / secondsInPeriod).toInt())

    override fun clock(): Flow<Long> = flow {
        while (true) {
            delay(1000)
            val timestamp = now()
            emit(timestamp)
        }
    }
}