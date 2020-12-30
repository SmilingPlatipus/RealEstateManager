package com.openclassrooms.realestatemanager.dao

import android.content.ContentValues
import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.model.EstatePhoto
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@Dao
interface EstateDao{
    @Query("DELETE FROM Estate WHERE id = :estateId")
    fun deleteItem(estateId: Long): Int

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
                    emptyList(),
                    emptyList(),
                    emptyList(),
                    values?.getAsString("status"),
                    null,
                    values?.getAsString("saleDate"),
                    values?.getAsString("saler"),
                    values?.getAsDouble("latitude"),
                    values?.getAsDouble("longitude")
            )
            values?.getAsString("photosUriWithDescriptions")?.let { newEstate.photosUriWithDescriptions = PhotoConverter.restoreList(it) }
            values?.getAsString("address")?.let { newEstate.address = ListOfStringConverter.restoreList(it) }
            values?.getAsString("poi")?.let { newEstate.poi = ListOfStringConverter.restoreList(it) }
            values?.getAsString("creationDate")?.let { newEstate.creationDate = OffsetDateTimeConverter.toOffsetDateTime(it) }
            return newEstate
        }

    }
}


object OffsetDateTimeConverter {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mmX").withZone(ZoneOffset.UTC)

    fun toOffsetDateTime(value: String?): OffsetDateTime? {
        return value?.let {
            return formatter.parse(value, OffsetDateTime::from)
        }
    }
}

object ListOfStringConverter {

    fun restoreList(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listType)
    }
}

object PhotoConverter {
    fun restoreList(value: String): List<EstatePhoto> {
        val listType = object : TypeToken<List<EstatePhoto>>() {}.type
        return Gson().fromJson(value, listType)
    }
}