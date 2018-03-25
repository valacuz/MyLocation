package com.example.valacuz.mylocations.data.repository

import com.example.valacuz.mylocations.data.PlaceItem
import io.reactivex.Completable
import io.reactivex.Flowable

interface PlaceDataSource : TimingsDataSource {

    fun getAllPlaces(): Flowable<List<PlaceItem>>

    fun getById(placeId: String): Flowable<PlaceItem>

    fun addPlace(place: PlaceItem): Completable

    fun addPlaces(places: List<PlaceItem>): Completable

    fun updatePlace(place: PlaceItem): Completable

    fun deletePlace(place: PlaceItem): Completable

    fun clearPlaces(): Completable
}