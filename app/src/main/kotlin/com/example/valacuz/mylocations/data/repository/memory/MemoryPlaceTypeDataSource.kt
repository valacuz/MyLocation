package com.example.valacuz.mylocations.data.repository.memory

import com.example.valacuz.mylocations.data.PlaceType
import com.example.valacuz.mylocations.data.repository.PlaceTypeDataSource
import io.reactivex.Flowable

class MemoryPlaceTypeDataSource private constructor() : PlaceTypeDataSource {

    private val types: MutableList<PlaceType> = mutableListOf()

    override fun getAllTypes(): Flowable<List<PlaceType>> = Flowable.fromArray(types)

    override fun addTypes(types: List<PlaceType>) {
        this.types.addAll(types)
    }

    companion object {

        @Volatile
        private var INSTANCE: MemoryPlaceTypeDataSource? = null

        fun getInstance(): MemoryPlaceTypeDataSource =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: MemoryPlaceTypeDataSource().also { INSTANCE = it }
                }
    }
}