package com.openclassrooms.realestatemanager.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.openclassrooms.realestatemanager.model.Estate

@Dao
interface EstateDao{
    @Query("SELECT * FROM estate")
    fun getAll(): LiveData<List<Estate>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(estate: Estate): Long

    @Update
    suspend fun update(estate: Estate)

    @Query("DELETE FROM estate")
    fun deleteAll()
}