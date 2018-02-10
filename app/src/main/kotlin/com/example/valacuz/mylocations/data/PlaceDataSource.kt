package com.example.valacuz.mylocations.data

import io.reactivex.Flowable

interface PlaceDataSource {

    fun getAllPlaces(): Flowable<List<PlaceItem>>

    fun getAllTypes(): Flowable<List<PlaceType>>

    fun getById(placeId: String): Flowable<PlaceItem>

    fun addPlace(placeItem: PlaceItem)

    fun updatePlace(placeItem: PlaceItem)

    fun deletePlace(placeItem: PlaceItem)

    fun clearPlaces()
}