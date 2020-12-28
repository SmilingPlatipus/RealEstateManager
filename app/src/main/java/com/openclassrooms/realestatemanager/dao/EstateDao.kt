package com.openclassrooms.realestatemanager.dao

import android.content.ContentValues
import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.model.EstatePhoto
import java.time.OffsetDateTime

@Dao
interface EstateDao{
    @Query("SELECT * FROM estate")
    fun getAll(): LiveData<MutableList<Estate>>

    @Query("SELECT * FROM estate WHERE id = (:estateId)")
    fun getEstatesWithCursor(estateId: Long) : Cursor

    @Query("SELECT * FROM estate WHERE id = (:estateId)")
    fun getEstateById(estateId: Long): Estate

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(estate: Estate): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(estate: Estate)

    @RawQuery
    fun searchForEstate(query : SupportSQLiteQuery) : MutableList<Estate>?

    companion object utils{
        fun estateFromContentValues(values : ContentValues?) : Estate{
            lateinit var newEstate : Estate
            newEstate = Estate(
                    values?.getAsString("type"),
                    values?.getAsFloat("price"),
                    values?.getAsFloat("size"),
                    values?.getAsInteger("rooms"),
                    values?.getAsString("description"),
                    values?.get("photosUriWithDescriptions") as List<EstatePhoto>?,
                    values?.get("address") as List<String>?,
                    values?.get("poi") as List<String>?,
                    values?.getAsString("status"),
                    values?.get("creationDate") as OffsetDateTime?,
                    values?.getAsString("saleDate"),
                    values?.getAsString("saler"),
                    values?.getAsDouble("latitude"),
                    values?.getAsDouble("latitude")
            )
            return newEstate
        }
    }
}