package com.pamparamm.habittracker.domain.services

import kotlinx.coroutines.flow.Flow

interface TimestampService {
    fun now(): Long
    fun periodsBetween(now: Long, then: Long): Int
    fun clock(): Flow<Long>
}