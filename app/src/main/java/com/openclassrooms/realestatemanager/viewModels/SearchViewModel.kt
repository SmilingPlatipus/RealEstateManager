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
                        estateRooms_max: Float): MutableList<Estate>?
    {
        var queryResult = repository.searchForEstate(
                estateType,
                estateStatus,
                estatePrice_min,
                estatePrice_max,
                estateSize_min,
                estateSize_max,
                estateRooms_min,
                estateRooms_max)

        queryResult?.forEach {
            val poiType = object : TypeToken<MutableList<String>>() {}.type
            var poiFromResult : MutableList<String> = Gson().fromJson(it.poi.toString(),poiType)
            //Log.i(TAG, "searchForEstate: $poiFromResult")

            // Todo : retrieve list of String from json and compare to estatePoi
            // Todo : need to handle "all" for estate status

            poiFromResult.forEach{
                for ((i, element) in it.withIndex())
                    if (!it.contains(estatePoi[i])){
                        poiFromResult.removeAt(i)
                        break
                    }
            }

            }
        return queryResult
    }
}

