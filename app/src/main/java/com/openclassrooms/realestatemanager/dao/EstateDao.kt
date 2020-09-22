package com.openclassrooms.realestatemanager.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.openclassrooms.realestatemanager.model.Estate

@Dao
interface EstateDao{
    @Query("SELECT * FROM estate")
    fun getAll() :LiveData<List<Estate>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(estate: Estate)
}