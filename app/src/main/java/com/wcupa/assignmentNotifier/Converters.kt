package com.wcupa.assignmentNotifier

import androidx.room.TypeConverter
import java.util.Date

//Converts Date object, allowing it to be stored with Room
class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }
}