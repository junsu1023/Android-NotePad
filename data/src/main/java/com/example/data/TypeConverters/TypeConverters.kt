package com.example.data.TypeConverters

import androidx.room.TypeConverter
import com.google.gson.Gson
import java.util.Date

class TypeConverters {
    @TypeConverter
    fun dateToLong(value: Date?) = value?.time

    @TypeConverter
    fun longToDate(value: Long?) = value?.let { Date(it) }

    @TypeConverter
    fun listToJson(value: List<String>?) = value?.let { Gson().toJson(it) }

    @TypeConverter
    fun jsonToList(value: String?) = value?.let { Gson().fromJson(it, Array<String>::class.java).toList() }
}