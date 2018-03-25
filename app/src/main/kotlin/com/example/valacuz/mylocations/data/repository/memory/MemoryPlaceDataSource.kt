package com.example.valacuz.mylocations.data.repository.memory

import com.example.valacuz.mylocations.data.PlaceItem
import com.example.valacuz.mylocations.data.repository.PlaceDataSource
import io.reactivex.Completable
import io.reactivex.Flowable

class MemoryPlaceDataSource private constructor() : PlaceDataSource {

    private val items: MutableList<PlaceItem> = mutableListOf()

    private var ticks: Long = 0

    override fun getAllPlaces(): Flowable<List<PlaceItem>> = Flowable.fromArray(items)

    override fun getById(placeId: String): Flowable<PlaceItem> {
        val placeItem: PlaceItem? = items.firstOrNull { it.id == placeId }
        return if (placeItem == null) {
            Flowable.empty()
        } else {
            Flowable.just(placeItem)
        }
    }

    override fun addPlace(place: PlaceItem): Completable {
        return if (!items.contains(place)) {
            val currentSize = items.size
            items.add(place)
            // If place add successfully, new size must be higher than previous size.
            if (items.size > currentSize)
                Completable.complete()
            else
                Completable.error(Throwable("Cannot add new place."))
        } else {
            updatePlace(place)
        }
    }

    override fun addPlaces(places: List<PlaceItem>): Completable {
        val currentSize = items.size
        // If place add successfully, new size must be higher than previous size.
        items.clear()
        items.addAll(places)
        ticks = System.currentTimeMillis()
        return if (items.size > currentSize)
            Completable.complete()
        else
            Completable.error(Throwable("Cannot add new places."))
    }

    override fun updatePlace(place: PlaceItem): Completable {
        val index = items.indexOfFirst { it.id == place.id }
        return if (index >= 0) {
            items[index] = place
            Completable.complete()
        } else {
            Completable.error(Throwable("Cannot update place."))
        }
    }

    override fun deletePlace(place: PlaceItem): Completable {
        return Completable.fromAction({
            if (items.removeAll { it.id == place.id })
                Completable.complete()
            else
                Completable.error(Throwable("Place not found."))
        })
    }

    override fun clearPlaces(): Completable {
        return Completable.fromAction({
            items.clear()
            if (items.size == 0)    // Recheck data are removed?
                Completable.complete()
            else
                Completable.error(Throwable("Cannot delete one or more places."))
        })
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