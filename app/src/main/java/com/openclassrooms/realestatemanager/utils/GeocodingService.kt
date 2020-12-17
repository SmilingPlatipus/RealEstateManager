package com.openclassrooms.realestatemanager.utils

import com.openclassrooms.realestatemanager.model.GeocodingResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

public interface GeocodingService {
    @GET("/maps/api/geocode/json")
    fun getLatLngFromAddress(@Query("address")address : String, @Query("key")key : String) : Call<GeocodingResponse>

    companion object {
        const val BASE_URL = "https://maps.googleapis.com"
    }
}