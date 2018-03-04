package com.example.valacuz.mylocations.data.repository

import com.example.valacuz.mylocations.data.PlaceItem
import com.example.valacuz.mylocations.data.PlaceType
import io.reactivex.Flowable

interface PlaceDataSource {

    fun getAllPlaces(): Flowable<List<PlaceItem>>

    fun getById(placeId: String): Flowable<PlaceItem>

    fun addPlace(place: PlaceItem)

    fun addPlaces(places: List<PlaceItem>)

    fun updatePlace(place: PlaceItem)

    fun deletePlace(place: PlaceItem)

    fun clearPlaces()
}