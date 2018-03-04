package com.example.valacuz.mylocations.data.repository.remote

import com.example.valacuz.mylocations.data.PlaceItem
import com.example.valacuz.mylocations.data.repository.PlaceDataSource
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable

class RemotePlaceDataSource private constructor(
        private val samplePlaceService: SamplePlaceService) : PlaceDataSource {

    override fun getAllPlaces(): Flowable<List<PlaceItem>> =
            samplePlaceService
                    .getPlaces()
                    .map({ responseItems: List<PlaceResponse> ->
                        responseItems.map {
                            PlaceItem(it.name, it.latitude, it.longitude, it.type, it.starred, it.id)
                        }
                    })
                    .toFlowable(BackpressureStrategy.LATEST)

    override fun getById(placeId: String): Flowable<PlaceItem> =
            samplePlaceService
                    .getPlace(placeId)
                    .map({ item: PlaceResponse ->
                        PlaceItem(item.name, item.latitude, item.longitude, item.type, item.starred, item.id)
                    })
                    .toFlowable(BackpressureStrategy.LATEST)

    override fun addPlace(place: PlaceItem) {
        val body = hashMapOf(
                "place_id" to place.id,
                "place_name" to place.name,
                "place_type" to place.type,
                "latitude" to place.latitude,
                "longitude" to place.longitude,
                "starred" to place.isStarred
        )
        samplePlaceService.addPlace(body)
    }

    override fun addPlaces(places: List<PlaceItem>) {
        // Operation not support on remote.
    }

    override fun updatePlace(place: PlaceItem) {
        // Operation not support on remote.
    }

    override fun deletePlace(place: PlaceItem) {
        samplePlaceService.deletePlace(place.id)
    }

    override fun clearPlaces() {
        samplePlaceService.clearPlaces()
    }
}