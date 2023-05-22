package com.pamparamm.habittracker.domain

import java.util.UUID

object UUIDExtensions {
    fun emptyUUID(): UUID = UUID(0, 0)
}