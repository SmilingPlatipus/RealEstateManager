package com.openclassrooms.realestatemanager.repositories

import androidx.lifecycle.LiveData
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.openclassrooms.realestatemanager.activities.SearchActivity
import com.openclassrooms.realestatemanager.dao.EstateDao
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.model.SearchEstateQuery
import java.util.*

class EstateDataRepository (private val estateDao: EstateDao){

    val allEstates: LiveData<MutableList<Estate>> = estateDao.getAll()

    fun getEstateById(estateId: Long): Estate{
        return estateDao.getEstateById(estateId)
    }

    suspend fun insert(estate: Estate): Long{
        return estateDao.insert(estate)
    }

    suspend fun update(estate: Estate){
        estateDao.update(estate)
    }

    fun searchForEstate(estateType : String,
                        estateStatus : String,
                        estatePrice_min : Float,
                        estatePrice_max : Float,
                        estateSize_min : Float,
                        estateSize_max : Float,
                        estateRooms_min : Float,
                        estateRooms_max : Float): MutableList<Estate>? {
        val searchEstateQuery = SimpleSQLiteQuery(
            "SELECT * FROM estate "+
                    "WHERE type IN ('$estateType') "+
                    "AND status IN ('$estateStatus', ${SearchActivity.SearchStatus.ALL.name.toLowerCase()}) "+
                    "AND price BETWEEN $estatePrice_min AND $estatePrice_max "+
                    "AND size BETWEEN $estateSize_min AND $estateSize_max "+
                    "AND rooms BETWEEN $estateRooms_min AND $estateRooms_max", arrayOf<SearchEstateQuery>())
        return estateDao.searchForEstate(searchEstateQuery)
    }


}