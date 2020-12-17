package com.openclassrooms.realestatemanager.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class GeocodingResponse {
    @SerializedName("results")
    @Expose
    var results: List<Result>? = null
}

class Result {
    @SerializedName("geometry")
    @Expose
    var geometry: Geometry? = null
}

class Geometry {
    @SerializedName("location")
    @Expose
    var location: Location? = null
}

class Location {
    @SerializedName("lat")
    @Expose
    var lat: Double? = null

    @SerializedName("lng")
    @Expose
    var lng: Double? = null
}