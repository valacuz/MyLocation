package com.example.valacuz.mylocations.data.repository.remote

import io.reactivex.Observable
import retrofit2.http.*

interface SamplePlaceService {

    // https://private-96860-valacuz.apiary-mock.com/places

    @GET("places")
    fun getPlaces(): Observable<List<PlaceResponse>>

    @GET("places/{id}")
    fun getPlace(@Query("id") id: String): Observable<PlaceResponse>

    @POST("places")
    fun addPlace(@Body body: Map<String, Any?>)

    @DELETE("places/{id}")
    fun deletePlace(@Query("id") id: String)

    @DELETE("places")
    fun clearPlaces()

    @GET("types")
    fun getPlaceTypes(): Observable<List<PlaceTypeResponse>>
}