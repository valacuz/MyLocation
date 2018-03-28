package com.example.valacuz.mylocations.data.repository.memory

import com.example.valacuz.mylocations.data.PlaceItem
import com.example.valacuz.mylocations.data.repository.PlaceDataSource
import io.reactivex.Completable
import io.reactivex.Flowable

open class MemoryPlaceDataSource internal constructor() : PlaceDataSource {

    private val items: MutableList<PlaceItem> = mutableListOf()

    private var ticks: Long = 0

    protected fun initializePlaces(vararg places: PlaceItem) {
        items.addAll(places)
    }

    override fun getAllPlaces(): Flowable<List<PlaceItem>> = Flowable.fromArray(items)

    override fun getById(placeId: String): Flowable<PlaceItem> {
        return Flowable.defer {
            val placeItem: PlaceItem? = items.firstOrNull { it.id == placeId }
            if (placeItem == null) {
                Flowable.never()
            } else {
                Flowable.just(placeItem)
            }
        }
    }

    override fun addPlace(place: PlaceItem): Completable {
        return Completable.defer {
            if (!items.contains(place)) {
                val currentSize = items.size
                items.add(place)
                // If place add successfully, new size must be higher than previous size.
                if (items.size > currentSize) {
                    Completable.complete()
                } else {
                    Completable.error(Throwable("Cannot add new place."))
                }
            } else {
                updatePlace(place)
            }
        }
    }

    override fun addPlaces(places: List<PlaceItem>): Completable {
        return Completable.defer {
            items.clear()
            items.addAll(places)
            ticks = System.currentTimeMillis()
            Completable.complete()
        }
    }

    override fun updatePlace(place: PlaceItem): Completable {
        return Completable.defer {
            val index = items.indexOfFirst { it.id == place.id }
            if (index >= 0) {
                items[index] = place
                Completable.complete()
            } else {
                Completable.error(Throwable("Cannot update place."))
            }
        }
    }

    override fun deletePlace(place: PlaceItem): Completable {
        return Completable.defer {
            if (items.removeAll { it.id == place.id }) {
                Completable.complete()
            } else {
                Completable.error(Throwable("Place not found."))
            }
        }
    }

    override fun clearPlaces(): Completable {
        return Completable.defer {
            items.clear()
            if (items.isEmpty()) {
                Completable.complete()
            } else {
                Completable.error(Throwable("Cannot delete one or more places."))
            }
        }
    }

    // Check is data is dirty (default 5 minutes or when data is empty)
    override fun isDirty() =
            System.currentTimeMillis() - ticks > (5 * 60 * 1_000) || items.isEmpty()

    companion object {

        @Volatile
        private var INSTANCE: MemoryPlaceDataSource? = null

        fun getInstance(): MemoryPlaceDataSource =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: MemoryPlaceDataSource()
                            .also { INSTANCE = it }
                }
    }
}