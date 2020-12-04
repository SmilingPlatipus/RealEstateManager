package com.openclassrooms.realestatemanager.repositories

import androidx.lifecycle.LiveData
import androidx.sqlite.db.SupportSQLiteQuery
import com.openclassrooms.realestatemanager.dao.EstateDao
import com.openclassrooms.realestatemanager.model.Estate

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

    fun searchForEstate(query: SupportSQLiteQuery): MutableList<Estate> {
        return estateDao.searchForEstate(query)
    }
}