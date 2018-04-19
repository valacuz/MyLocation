package com.example.valacuz.mylocations.model

import com.example.valacuz.mylocations.data.repository.PlaceTypeDataSource
import com.example.valacuz.mylocations.data.repository.memory.MemoryPlaceTypeDataSource

class MemoryPlaceTypeDataSourceTest : PlaceTypeDataSourceTest() {

    override fun doPrepare() {
        // Do nothing
    }

    override fun doCleanUp() {
        // Do nothing
    }

    override fun getPlaceTypeDataSource(): PlaceTypeDataSource {
        return MemoryPlaceTypeDataSource()
    }
}