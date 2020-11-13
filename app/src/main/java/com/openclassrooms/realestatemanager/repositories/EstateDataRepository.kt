package com.openclassrooms.realestatemanager.repositories

import androidx.lifecycle.LiveData
import com.openclassrooms.realestatemanager.dao.EstateDao
import com.openclassrooms.realestatemanager.model.Estate

class EstateDataRepository (private val estateDao: EstateDao){

    val allEstates: LiveData<MutableList<Estate>> = estateDao.getAll()

    suspend fun insert(estate: Estate): Long{
        return estateDao.insert(estate)
    }

    suspend fun update(estate: Estate){
        estateDao.update(estate)
    }

    suspend fun deleteAll() {
        estateDao.deleteAll()
    }
}