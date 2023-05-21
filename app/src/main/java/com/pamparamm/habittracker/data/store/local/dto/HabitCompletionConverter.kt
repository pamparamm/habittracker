package com.pamparamm.habittracker.data.store.local.dto

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class HabitCompletionConverter {
    @TypeConverter
    fun fromJson(value: String): List<Long> {
        val listType = object : TypeToken<List<Long>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun toJson(list: List<Long>): String {
        return Gson().toJson(list)
    }
}