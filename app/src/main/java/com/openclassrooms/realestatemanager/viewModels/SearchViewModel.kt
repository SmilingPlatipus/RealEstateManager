package com.openclassrooms.realestatemanager.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.repositories.EstateDataRepository

class SearchViewModel(estateDataRepository: EstateDataRepository) : ViewModel() {
    private val repository: EstateDataRepository = estateDataRepository
    val allEstates: LiveData<MutableList<Estate>>

    init {
        allEstates = repository.allEstates
    }


}

