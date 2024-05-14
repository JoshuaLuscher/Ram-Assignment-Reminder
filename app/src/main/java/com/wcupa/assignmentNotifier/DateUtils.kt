package com.wcupa.assignmentNotifier

import android.util.Log
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

class DateUtils {

    //Converts a Long of Milliseconds into a LocalDate object
    fun convertMillisToLocalDate(millis: Long) : LocalDate {
        Log.d("millis", millis.toString())
        return Instant
            .ofEpochMilli(millis)
            .atZone(ZoneOffset.UTC)
            .toLocalDate()
    }

    //Converts Milliseconds to LocalDate with with a date formatter
    private fun convertMillisToLocalDateWithFormatter(date: LocalDate, dateTimeFormatter: DateTimeFormatter) : LocalDate {
        val dateInMillis = LocalDate.parse(date.format(dateTimeFormatter), dateTimeFormatter)
            .atStartOfDay(ZoneOffset.UTC)
            .toInstant()
            .toEpochMilli()

        //Convert the millis to a localDate object
        return Instant
            .ofEpochMilli(dateInMillis)
            .atZone(ZoneOffset.UTC)
            .toLocalDate()
    }


    //Converts date to a string
    fun dateToString(date: LocalDate): String {
        val dateFormatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM, yyyy", Locale.getDefault())
        val dateInMillis = convertMillisToLocalDateWithFormatter(date, dateFormatter)
        return dateFormatter.format(dateInMillis)
    }

    //Converts LocalDate object to Date object
    fun localDateToDate(localDate: LocalDate): Date {
        Log.d("Local Date", localDate.toString())
        return Date.from(localDate.atStartOfDay(ZoneOffset.UTC).toInstant())
    }
}