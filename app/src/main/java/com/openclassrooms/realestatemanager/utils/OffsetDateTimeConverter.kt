package com.openclassrooms.realestatemanager.utils

import androidx.room.TypeConverter
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

open class OffsetDateTimeConverter {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mmX")
            .withZone(ZoneOffset.UTC)


    @TypeConverter
    fun toOffsetDateTime(value: String?): OffsetDateTime? {
        return value?.let {
            return formatter.parse(value, OffsetDateTime::from)
        }
    }

    @TypeConverter
    fun fromOffsetDateTime(date: OffsetDateTime?): String? {
        return date?.let {
            it.format(formatter)
        }
    }
}