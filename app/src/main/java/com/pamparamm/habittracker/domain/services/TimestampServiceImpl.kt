package com.pamparamm.habittracker.domain.services

import java.time.Clock
import java.time.Instant
import javax.inject.Inject

class TimestampServiceImpl @Inject constructor() : TimestampService {
    override fun now(): Long {
        val clock = Clock.systemUTC()
        val instant = Instant.now(clock)
        return instant.epochSecond
    }
}