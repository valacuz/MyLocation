package com.example.valacuz.mylocations.data.repository.memory

import com.example.valacuz.mylocations.data.PlaceType
import com.example.valacuz.mylocations.data.repository.PlaceTypeDataSource
import io.reactivex.Flowable

class MemoryPlaceTypeDataSource private constructor() : PlaceTypeDataSource {

    private val types: MutableList<PlaceType> = mutableListOf()

    private var ticks: Long = 0

    override fun getAllTypes(): Flowable<List<PlaceType>> = Flowable.fromArray(types)

    override fun addTypes(types: List<PlaceType>) {
        this.types.addAll(types)
        ticks = System.currentTimeMillis()
    }

    // Check is data is dirty (default 5 minutes or when data is empty)
    override fun isDirty() =
            System.currentTimeMillis() - ticks > (5 * 60 * 1_000) || types.isEmpty()

    companion object {

        @Volatile
        private var INSTANCE: MemoryPlaceTypeDataSource? = null

        fun getInstance(): MemoryPlaceTypeDataSource =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: MemoryPlaceTypeDataSource().also { INSTANCE = it }
                }
    }
}