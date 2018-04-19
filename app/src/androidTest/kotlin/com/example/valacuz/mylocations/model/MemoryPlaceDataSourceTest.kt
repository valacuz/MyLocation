package com.example.valacuz.mylocations.model

import com.example.valacuz.mylocations.data.repository.PlaceDataSource
import com.example.valacuz.mylocations.data.repository.memory.MemoryPlaceDataSource

class MemoryPlaceDataSourceTest : PlaceDataSourceTest() {

    override fun doPrepare() {
        // Do nothing
    }

    override fun doCleanUp() {
        // Do nothing
    }

    override fun getPlaceDataSource(): PlaceDataSource {
        return MemoryPlaceDataSource()
    }
}