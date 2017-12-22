package com.example.valacuz.mylocations.data.repository

import com.example.valacuz.mylocations.data.PlaceDataSource
import com.example.valacuz.mylocations.data.PlaceItem
import com.example.valacuz.mylocations.data.PlaceType

class MemoryPlaceDataSource private constructor() : PlaceDataSource {

    private val items: MutableList<PlaceItem> = mutableListOf()
           /* mutableListOf(
            PlaceItem(
                    "Chulalongkorn University",
                    13.741734,
                    100.516680,
                    1,
                    false),
            PlaceItem("MBK",
                    13.743490,
                    100.530778,
                    2,
                    false),
            PlaceItem("Dice Cup Board Game Cafe",
                    13.743240,
                    100.527709,
                    4,
                    false),
            PlaceItem("Siam discovery",
                    13.743490,
                    100.530778,
                    2,
                    false),
            PlaceItem("Dak Galbi",
                    13.744298,
                    100.534855,
                    3,
                    false))*/

    private val types: MutableList<PlaceType> = mutableListOf(
            PlaceType(1, "Education"),
            PlaceType(2, "Department store"),
            PlaceType(3, "Restaurant"),
            PlaceType(4, "Relaxation")
    )

    override fun getAllPlaces(): List<PlaceItem>? = items

    override fun getAllTypes(): List<PlaceType>? = types

    override fun getById(placeId: String) = items.firstOrNull { it.id == placeId }

    override fun addPlace(placeItem: PlaceItem) {
        if (!items.contains(placeItem))
            items.add(placeItem)
    }

    override fun updatePlace(placeItem: PlaceItem) {
        val index = items.indexOfFirst { it.id == placeItem.id }
        if (index >= 0)
            items[index] = placeItem
    }

    override fun deletePlace(placeItem: PlaceItem) {
        items.removeAll { it.id == placeItem.id }
    }

    override fun clearPlace() {
        items.clear()
    }

    companion object {
        val INSTANCE: MemoryPlaceDataSource by lazy {
            MemoryPlaceDataSource()
        }
    }
}