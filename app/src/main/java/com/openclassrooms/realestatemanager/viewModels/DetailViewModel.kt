package com.openclassrooms.realestatemanager.viewModels

import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.repositories.EstateDataRepository

class DetailViewModel(estateDataRepository: EstateDataRepository) : ViewModel() {
    private val repository: EstateDataRepository = estateDataRepository

    fun getEstateById(id: Long): Estate {
        return repository.getEstateById(id) as Estate
    }
}