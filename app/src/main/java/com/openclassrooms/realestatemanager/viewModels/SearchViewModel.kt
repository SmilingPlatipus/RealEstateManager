package com.openclassrooms.realestatemanager.viewModels

import android.annotation.TargetApi
import android.os.Build
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.openclassrooms.realestatemanager.activities.SearchActivity
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.repositories.EstateDataRepository
import com.openclassrooms.realestatemanager.utils.ListOfStringConverter
import java.util.*

class SearchViewModel(estateDataRepository: EstateDataRepository) : ViewModel() {
    private val TAG = "SearchViewModel"
    private val repository: EstateDataRepository = estateDataRepository
    val allEstates: LiveData<MutableList<Estate>>

    init {
        allEstates = repository.allEstates
    }

    @TargetApi(Build.VERSION_CODES.N)
    fun searchForEstate(estatePoi: MutableList<String>,
                        estateType: String,
                        estateStatus: String,
                        estatePrice_min: Float,
                        estatePrice_max: Float,
                        estateSize_min: Float,
                        estateSize_max: Float,
                        estateRooms_min: Float,
                        estateRooms_max: Float): MutableList<Estate>? {
        var queryResult = repository.searchForEstate(
                estateType,
                estateStatus,
                estatePrice_min,
                estatePrice_max,
                estateSize_min,
                estateSize_max,
                estateRooms_min,
                estateRooms_max)

        var elementsToRemove = mutableListOf<Estate>()
        queryResult?.forEach {
            val poiType = object : TypeToken<MutableList<String>>() {}.type
            var poisFromResult: MutableList<String> = Gson().fromJson(it.poi.toString(), poiType)

            if (!poisFromResult.containsAll(estatePoi)) {
                elementsToRemove.add(it)
            }
        }
        queryResult?.removeAll(elementsToRemove)
        if (queryResult.isNullOrEmpty())
            return null
        else
        return queryResult
    }
}

