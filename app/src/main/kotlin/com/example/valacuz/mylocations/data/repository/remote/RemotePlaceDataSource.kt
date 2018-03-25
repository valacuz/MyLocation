package com.example.valacuz.mylocations.data.repository.remote

import com.example.valacuz.mylocations.data.PlaceItem
import com.example.valacuz.mylocations.data.repository.PlaceDataSource
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
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

    override fun addPlace(place: PlaceItem): Completable {
        val body = hashMapOf(
                "place_id" to place.id,
                "place_name" to place.name,
                "place_type" to place.type,
                "latitude" to place.latitude,
                "longitude" to place.longitude,
                "starred" to place.isStarred
        )
        return samplePlaceService.addPlace(body)
    }

    // Operation not support on remote, so we always return complete.
    override fun addPlaces(places: List<PlaceItem>): Completable = Completable.complete()

    override fun updatePlace(place: PlaceItem): Completable {
        // Operation not support on remote.
        val body = hashMapOf(
                "place_id" to place.id,
                "place_name" to place.name,
                "place_type" to place.type,
                "latitude" to place.latitude,
                "longitude" to place.longitude,
                "starred" to place.isStarred
        )
        return samplePlaceService.updatePlace(place.id, body)
    }

    override fun deletePlace(place: PlaceItem): Completable {
        return samplePlaceService.deletePlace(place.id)
    }

    override fun clearPlaces(): Completable {
        return samplePlaceService.clearPlaces()
    }

    override fun isDirty(): Boolean = false // Never!

    companion object {

        @Volatile
        private var INSTANCE: RemotePlaceDataSource? = null

        fun getInstance(samplePlaceService: SamplePlaceService) =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: RemotePlaceDataSource(samplePlaceService)
                            .also { INSTANCE = it }
                }
    }
}