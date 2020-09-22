package com.openclassrooms.realestatemanager.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.db.EstateDatabase
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.repositories.EstateDataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EstateViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: EstateDataRepository
    val allEstates: LiveData<List<Estate>>

    init {
        val estatesDao = EstateDatabase.getEstateDataBase(application, viewModelScope).estateDao()
        repository = EstateDataRepository(estatesDao)
        allEstates = repository.allEstates
    }

    fun insert(estate: Estate) = viewModelScope.launch (Dispatchers.IO){
        repository.insert(estate)
    }
}