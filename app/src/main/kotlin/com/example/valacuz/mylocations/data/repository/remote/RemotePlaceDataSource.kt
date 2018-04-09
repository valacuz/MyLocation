package com.example.valacuz.mylocations.data.repository.remote

import com.example.valacuz.mylocations.data.PlaceItem
import com.example.valacuz.mylocations.data.repository.PlaceDataSource
import com.example.valacuz.mylocations.extension.toPlaceItem
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Flowable

class RemotePlaceDataSource private constructor(
        private val samplePlaceService: SamplePlaceService) : PlaceDataSource {

    override fun getAllPlaces(): Flowable<List<PlaceItem>> =
            samplePlaceService
                    .getPlaces()
                    .map { items: List<PlaceResponse> ->
                        items.map { it.toPlaceItem() }
                    }
                    .toFlowable(BackpressureStrategy.LATEST)

    override fun getById(placeId: String): Flowable<PlaceItem> =
            samplePlaceService
                    .getPlace(placeId)
                    .map { it.toPlaceItem() }
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

    override fun deletePlace(place: PlaceItem): Completable = samplePlaceService.deletePlace(place.id)

    override fun clearPlaces(): Completable = samplePlaceService.clearPlaces()

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