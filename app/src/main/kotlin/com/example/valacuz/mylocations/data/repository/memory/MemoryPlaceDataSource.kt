package com.example.valacuz.mylocations.data.repository.memory

import com.example.valacuz.mylocations.data.PlaceItem
import com.example.valacuz.mylocations.data.repository.PlaceDataSource
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

    override fun addPlace(place: PlaceItem) {
        if (!items.contains(place)) {
            val currentSize = items.size
            items.add(place)

            // If place add successfully, new size must be higher than previous size.
            if (items.size > currentSize) {
                ticks = System.currentTimeMillis()
            } else {
                throw Throwable("Cannot add new place.")
            }
        } else {
            updatePlace(place)
        }
    }

    override fun addPlaces(places: List<PlaceItem>) {
        val currentSize = items.size
        items.addAll(places)

        // If place add successfully, new size must be higher than previous size.
        if (items.size > currentSize) {
            ticks = System.currentTimeMillis()
        } else {
            throw Throwable("Cannot add new place.")
        }
    }

    override fun updatePlace(place: PlaceItem) {
        val index = items.indexOfFirst { it.id == place.id }
        if (index >= 0) {
            items[index] = place
        } else {
            throw Throwable("Cannot update place. place not found.")
        }
    }

    override fun deletePlace(place: PlaceItem) {
        if (items.removeAll { it.id == place.id }) {
            ticks = System.currentTimeMillis()
        } else {
            throw Throwable("Cannot delete one or more place(s). Place not found.")
        }
    }

    override fun clearPlaces() {
        items.clear()
        if (items.isEmpty()) {
            ticks = System.currentTimeMillis()
        } else {
            throw Throwable("Cannot delete one or more place(s).")
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