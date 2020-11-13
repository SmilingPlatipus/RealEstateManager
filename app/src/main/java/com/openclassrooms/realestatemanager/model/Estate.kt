package com.openclassrooms.realestatemanager.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.openclassrooms.realestatemanager.utils.ListConverter
import com.openclassrooms.realestatemanager.utils.OffsetDateTimeConverter
import java.time.OffsetDateTime

@Entity
data class Estate (
        @PrimaryKey(autoGenerate = true)
        var id: Long = 0,
        var type :String,
        var price :Double,
        var size :Float,
        var rooms :Int,
        var description :String,
        var photoUri :String,
        @TypeConverters(ListConverter::class)
        var address :List<String>,
        @TypeConverters(ListConverter::class)
        var poi :List<String>,
        var status :String,
        @TypeConverters(OffsetDateTimeConverter::class)
        var creationDate :OffsetDateTime,
        @TypeConverters(OffsetDateTimeConverter::class)
        var saleDate :OffsetDateTime?,
        var dealer :String,
        var latitude :Double?,
        var longitude :Double?
)