package com.openclassrooms.realestatemanager.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


open class ListOfStringConverter {
    @TypeConverter
    fun restoreList(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun saveList(list: List<String>): String {
        return Gson().toJson(list)
    }
}