package com.openclassrooms.realestatemanager.repositories

import androidx.lifecycle.LiveData
import com.openclassrooms.realestatemanager.dao.EstateDao
import com.openclassrooms.realestatemanager.model.Estate

class EstateDataRepository (private val estateDao: EstateDao){

    val allEstates: LiveData<List<Estate>> = estateDao.getAll()

    suspend fun insert(estate: Estate){
        estateDao.insert(estate)
    }
}