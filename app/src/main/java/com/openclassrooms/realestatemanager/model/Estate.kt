package com.openclassrooms.realestatemanager.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.openclassrooms.realestatemanager.utils.ListOfEstatePhotoConverter
import com.openclassrooms.realestatemanager.utils.ListOfStringConverter
import com.openclassrooms.realestatemanager.utils.OffsetDateTimeConverter
import java.time.OffsetDateTime

@Entity
data class Estate (
        var type :String?,
        var price :Float?,
        var size :Float?,
        var rooms :Int?,
        var description :String?,
        @TypeConverters(ListOfEstatePhotoConverter::class)
        var photosUriWithDescriptions :List<EstatePhoto>?,
        @TypeConverters(ListOfStringConverter::class)
        var address :List<String>?,
        @TypeConverters(ListOfStringConverter::class)
        var poi :List<String>?,
        var status :String?,
        @TypeConverters(OffsetDateTimeConverter::class)
        var creationDate :OffsetDateTime?,
        var saleDate :String?,
        var saler :String?,
        var latitude :Double?,
        var longitude :Double?
){
        @PrimaryKey(autoGenerate = true)
        var id: Long = 0
}