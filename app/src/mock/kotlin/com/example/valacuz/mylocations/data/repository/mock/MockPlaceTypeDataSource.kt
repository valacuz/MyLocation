package com.example.valacuz.mylocations.data.repository.mock

import com.example.valacuz.mylocations.data.PlaceType
import com.example.valacuz.mylocations.data.repository.memory.MemoryPlaceTypeDataSource

class MockPlaceTypeDataSource private constructor() : MemoryPlaceTypeDataSource() {

    init {
        // I want the same logic as MemoryPlaceTypeDataSource but I'm lazy to create new one.
        initializeTypes(
                PlaceType(1, "Education"),
                PlaceType(2, "Department store"),
                PlaceType(3, "Restaurant"),
                PlaceType(4, "Relaxation")
        )
    }

    override fun isDirty(): Boolean = false // Never

    companion object {

        @Volatile
        private var INSTANCE: MockPlaceTypeDataSource? = null

        fun getInstance(): MockPlaceTypeDataSource =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: MockPlaceTypeDataSource().also { INSTANCE = it }
                }
    }
}