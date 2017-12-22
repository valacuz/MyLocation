package com.example.valacuz.mylocations.data

interface PlaceDataSource {

    fun getAllPlaces(): List<PlaceItem>?

    fun getAllTypes(): List<PlaceType>?

    fun getById(placeId: String): PlaceItem?

    fun addPlace(placeItem: PlaceItem)

    fun updatePlace(placeItem: PlaceItem)

    fun deletePlace(placeItem: PlaceItem)

    fun clearPlace()
}