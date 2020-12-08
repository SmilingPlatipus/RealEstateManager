package com.openclassrooms.realestatemanager.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.openclassrooms.realestatemanager.model.Estate

@Dao
interface EstateDao{
    @Query("SELECT * FROM estate")
    fun getAll(): LiveData<MutableList<Estate>>

    @Query("SELECT * FROM estate WHERE id = (:estateId)")
    fun getEstateById(estateId: Long): Estate

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(estate: Estate): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(estate: Estate)

    @RawQuery
    fun searchForEstate(query : SupportSQLiteQuery) : MutableList<Estate>?
}