package com.openclassrooms.realestatemanager.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.openclassrooms.realestatemanager.model.EstatePhoto

open class ListOfEstatePhotoConverter {
    @TypeConverter
    fun restoreList(value: String): List<EstatePhoto> {
        val listType = object : TypeToken<List<EstatePhoto>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun saveList(list: List<EstatePhoto>): String {
        return Gson().toJson(list)
    }
}