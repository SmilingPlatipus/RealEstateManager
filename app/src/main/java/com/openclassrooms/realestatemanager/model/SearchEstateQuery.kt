package com.openclassrooms.realestatemanager.model

import com.openclassrooms.realestatemanager.activities.SearchActivity

data class SearchEstateQuery(
        var estateType : SearchActivity.EstateTypes,
        var estateStatus : SearchActivity.SearchStatus,
        var estatePoi : SearchActivity.NearbyServices,
        var estatePrice_min : Int,
        var estatePrice_max : Int,
        var estateSize_min : Int,
        var estateSize_max : Int,
        var estateRooms_min : Int,
        var estateRooms_max : Int
) {

}