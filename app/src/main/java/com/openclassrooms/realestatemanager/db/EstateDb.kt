package com.openclassrooms.realestatemanager.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.openclassrooms.realestatemanager.dao.EstateDao
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.utils.ListConverter
import com.openclassrooms.realestatemanager.utils.OffsetDateTimeConverter

@Database(entities = [Estate::class], version = 1)
@TypeConverters(OffsetDateTimeConverter::class, ListConverter::class)
abstract class EstateDatabase : RoomDatabase() {
    abstract val estateDao: EstateDao
}
