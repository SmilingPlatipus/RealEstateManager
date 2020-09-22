package com.openclassrooms.realestatemanager.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Timestamp

@Entity
data class Estate (
        @PrimaryKey(autoGenerate = true)
        var id: Int?,
        var type :String?,
        var price :Double?,
        var size :Float?,
        var rooms :Int?,
        var description :String,
        var photoUri :String?,
        var address :String?,
        //var neighbourPoi :List<String>,
        var status :String?,
        //var creationDate :Timestamp,
        //var saleDate :Timestamp,
        var dealer :String?
)