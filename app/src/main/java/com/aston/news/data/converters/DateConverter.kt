package ru.itmo.notes.data.converters

import androidx.room.TypeConverter
import java.util.Calendar
import java.util.Date
import java.util.TimeZone

class CalendarConverter {

    @TypeConverter
    fun toTimestamp(date: Calendar): Long {
        return date.timeInMillis
    }

    @TypeConverter
    fun toCalendar(date: Long): Calendar {
        return Calendar.getInstance().apply { time = Date(date) }
    }

}