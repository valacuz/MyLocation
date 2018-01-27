package com.example.valacuz.mylocations.data.repository

import com.example.valacuz.mylocations.data.PlaceDataSource
import com.example.valacuz.mylocations.data.PlaceItem
import com.example.valacuz.mylocations.data.PlaceType
import io.reactivex.Flowable

class MemoryPlaceDataSource private constructor() : PlaceDataSource {

    private val items: MutableList<PlaceItem> = mutableListOf()

    private val types: List<PlaceType> = listOf(
            PlaceType(1, "Education"),
            PlaceType(2, "Department store"),
            PlaceType(3, "Restaurant"),
            PlaceType(4, "Relaxation")
    )

    override fun getAllPlaces(): Flowable<List<PlaceItem>> = Flowable.fromArray(items)

    override fun getAllTypes(): Flowable<List<PlaceType>> = Flowable.fromArray(types)

    override fun getById(placeId: String): Flowable<PlaceItem> {
        val placeItem: PlaceItem? = items.firstOrNull { it.id == placeId }
        return if (placeItem == null) {
            Flowable.empty()
        } else {
            Flowable.just(placeItem)
        }
    }

    override fun addPlace(placeItem: PlaceItem) {
        if (!items.contains(placeItem)) {
            items.add(placeItem)
        } else {
            return updatePlace(placeItem)
        }
    }

    override fun updatePlace(placeItem: PlaceItem) {
        val index = items.indexOfFirst { it.id == placeItem.id }
        if (index >= 0) {
            items[index] = placeItem
        }
    }

    override fun deletePlace(placeItem: PlaceItem) {
        items.removeAll { it.id == placeItem.id }
    }

    override fun clearPlaces() {
        items.clear()
    }

    companion object {
        val INSTANCE: MemoryPlaceDataSource by lazy {
            MemoryPlaceDataSource()
        }
    }
}