package com.example.valacuz.mylocations.data.repository.memory

import com.example.valacuz.mylocations.data.PlaceItem
import com.example.valacuz.mylocations.data.repository.PlaceDataSource
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

    override fun addPlace(place: PlaceItem) {
        if (!items.contains(place)) {
            items.add(place)
        } else {
            return updatePlace(place)
        }
    }

    override fun addPlaces(places: List<PlaceItem>) {
        items.addAll(places)
        ticks = System.currentTimeMillis()
    }

    override fun updatePlace(place: PlaceItem) {
        val index = items.indexOfFirst { it.id == place.id }
        if (index >= 0) {
            items[index] = place
        }
    }

    override fun deletePlace(place: PlaceItem) {
        items.removeAll { it.id == place.id }
    }

    override fun clearPlaces() {
        items.clear()
    }

    // Check is data is dirty (default 5 minutes or when data is empty)
    override fun isDirty() =
            System.currentTimeMillis() - ticks > (5 * 60 * 1_000) || items.isEmpty()

    companion object {

        @Volatile
        private var INSTANCE: MemoryPlaceDataSource? = null

        fun getInstance(): MemoryPlaceDataSource =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: MemoryPlaceDataSource().also { INSTANCE = it }
                }
    }
}