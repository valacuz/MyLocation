package com.example.valacuz.mylocations.data.repository.mock

import com.example.valacuz.mylocations.data.PlaceItem
import com.example.valacuz.mylocations.data.repository.memory.MemoryPlaceDataSource

class MockPlaceDataSource private constructor() : MemoryPlaceDataSource() {

    private val items = mutableListOf(
            PlaceItem("Chulalongkorn university", 13.7419273, 100.5256927, 1, true),
            PlaceItem("The old siam", 13.7492849, 100.4989994, 2, false),
            PlaceItem("Bobae Tower", 13.7492849, 100.4989994, 2, false),
            PlaceItem("Grand china hotel", 13.7423837, 100.5075352, 1, true))

    init {
        // I want the same logic as MemoryPlaceDataSource but I'm lazy to create new one.
        addPlaces(items)
    }

    override fun isDirty(): Boolean = false    // Never!

    companion object {

        @Volatile
        private var INSTANCE: MockPlaceDataSource? = null

        fun getInstance(): MockPlaceDataSource =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: MockPlaceDataSource().also { INSTANCE = it }
                }
    }
}