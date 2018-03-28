package com.example.valacuz.mylocations.data.repository

import com.example.valacuz.mylocations.data.PlaceItem
import io.reactivex.Completable
import io.reactivex.Flowable

/**
 * This class with three source of PlaceDataSource combined.
 *
 * Each data source has difference
 */
class CompositePlaceDataSource private constructor(
        private val memorySource: PlaceDataSource,
        private val localSource: PlaceDataSource,
        private val remoteSource: PlaceDataSource) : PlaceDataSource {

    override fun getAllPlaces(): Flowable<List<PlaceItem>> {
        val index = arrayOf(memorySource, localSource, remoteSource).indexOfFirst { !it.isDirty() }
        return when (index) {
            0 -> getAllPlacesFromMemory()
            1 -> getAllPlacesFromLocal()
            else -> getAllPlacesFromRemote()
        }
    }

    private fun getAllPlacesFromMemory() = memorySource.getAllPlaces()

    private fun getAllPlacesFromLocal() = localSource.getAllPlaces()
            .doOnNext({
                memorySource.addPlaces(it)
            })

    private fun getAllPlacesFromRemote() = remoteSource.getAllPlaces()
            .doOnNext({
                localSource.addPlaces(it)
                memorySource.addPlaces(it)
            })

    override fun getById(placeId: String): Flowable<PlaceItem> {
        val index = arrayOf(memorySource, localSource, remoteSource).indexOfFirst { !it.isDirty() }
        return when (index) {
            0 -> getByIdFromMemory(placeId)
            1 -> getByIdFromLocal(placeId)
            else -> getByIdFromRemote(placeId)
        }
    }

    private fun getByIdFromMemory(placeId: String) = memorySource.getById(placeId)

    private fun getByIdFromLocal(placeId: String) = localSource.getById(placeId)
            .doOnNext({
                memorySource.addPlace(it)
            })

    private fun getByIdFromRemote(placeId: String) = remoteSource.getById(placeId)
            .doOnNext({
                localSource.addPlace(it)
                memorySource.addPlace(it)
            })

    override fun addPlace(place: PlaceItem): Completable {
        return remoteSource.addPlace(place)
                .andThen(localSource.addPlace(place))
                .andThen(memorySource.addPlace(place))
    }

    override fun addPlaces(places: List<PlaceItem>): Completable {
        return remoteSource.addPlaces(places)
                .andThen(localSource.addPlaces(places))
                .andThen(memorySource.addPlaces(places))
    }

    override fun updatePlace(place: PlaceItem): Completable {
        return remoteSource.updatePlace(place)
                .andThen(localSource.updatePlace(place))
                .andThen(memorySource.updatePlace(place))
    }

    override fun deletePlace(place: PlaceItem): Completable {
        return remoteSource.deletePlace(place)
                .andThen(localSource.deletePlace(place))
                .andThen(memorySource.deletePlace(place))
    }

    override fun clearPlaces(): Completable {
        return remoteSource.clearPlaces()
                .andThen(localSource.clearPlaces())
                .andThen(memorySource.clearPlaces())
    }

    override fun isDirty(): Boolean = false // Never!

    companion object {

        @Volatile
        private var INSTANCE: CompositePlaceDataSource? = null

        fun getInstance(memorySource: PlaceDataSource,
                        localSource: PlaceDataSource,
                        remoteSource: PlaceDataSource) =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: CompositePlaceDataSource(
                            memorySource, localSource, remoteSource).also { INSTANCE = it }
                }

    }
}