package com.example.valacuz.mylocations.data.repository.remote

import io.reactivex.Completable
import io.reactivex.Observable
import retrofit2.http.*

interface SamplePlaceService {

    // https://private-96860-valacuz.apiary-mock.com/places

    @GET("places")
    fun getPlaces(): Observable<List<PlaceResponse>>

    @GET("places/{id}")
    fun getPlace(@Query("id") id: String): Observable<PlaceResponse>

    @POST("places")
    fun addPlace(@Body body: Map<String, @JvmSuppressWildcards Any?>): Completable

    @PUT("places/{id}")
    fun updatePlace(@Query("id") id: String, @Body body: Map<String, @JvmSuppressWildcards Any?>): Completable

    @DELETE("places/{id}")
    fun deletePlace(@Query("id") id: String): Completable

    @DELETE("places")
    fun clearPlaces(): Completable

    @GET("types")
    fun getPlaceTypes(): Observable<List<PlaceTypeResponse>>
}