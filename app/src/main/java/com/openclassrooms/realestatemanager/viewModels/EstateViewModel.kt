package com.openclassrooms.realestatemanager.viewModels

import androidx.lifecycle.*
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.repositories.EstateDataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EstateViewModel(estateDataRepository: EstateDataRepository) : ViewModel() {

    private val repository: EstateDataRepository = estateDataRepository
    val allEstates: LiveData<MutableList<Estate>>

    init {
        allEstates = repository.allEstates
    }

    fun insert(estate: Estate) = viewModelScope.launch (Dispatchers.IO){
        repository.insert(estate)
    }

    fun update(estate: Estate) = viewModelScope.launch (Dispatchers.IO){
        repository.update(estate)
    }

    fun deleteAll() = viewModelScope.launch (Dispatchers.IO){
        repository.deleteAll()
    }

    fun getEstateById(id: Long): Estate {
        return repository.getEstateById(id) as Estate
    }
}