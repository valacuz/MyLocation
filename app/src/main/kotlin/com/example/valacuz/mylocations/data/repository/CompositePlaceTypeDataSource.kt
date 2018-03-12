package com.example.valacuz.mylocations.data.repository

import com.example.valacuz.mylocations.data.PlaceType
import io.reactivex.Flowable

/**
 * The class with three source of PlaceTypeDataSource combined.
 */
class CompositePlaceTypeDataSource private constructor(
        private val memorySource: PlaceTypeDataSource,
        private val localSource: PlaceTypeDataSource,
        private val remoteSource: PlaceTypeDataSource) : PlaceTypeDataSource {

    override fun getAllTypes(): Flowable<List<PlaceType>> {
        val index = arrayOf(memorySource, localSource, remoteSource).indexOfFirst { !it.isDirty() }
        return when (index) {
            0 -> getAllTypesFromMemory()
            1 -> getAllTypesFromLocal()
            else -> getAllTypesFromRemote()
        }
    }

    private fun getAllTypesFromMemory() = memorySource.getAllTypes()

    private fun getAllTypesFromLocal() = localSource.getAllTypes()
            .doOnNext({
                memorySource.addTypes(it)
            })

    private fun getAllTypesFromRemote() = remoteSource.getAllTypes()
            .doOnNext({
                localSource.addTypes(it)
                memorySource.addTypes(it)
            })

    override fun addTypes(types: List<PlaceType>) {
        remoteSource.addTypes(types)
        localSource.addTypes(types)
        memorySource.addTypes(types)
    }

    override fun isDirty(): Boolean = false // Never!

    companion object {

        @Volatile
        private var INSTANCE: CompositePlaceTypeDataSource? = null

        fun getInstance(memorySource: PlaceTypeDataSource,
                        localSource: PlaceTypeDataSource,
                        remoteSource: PlaceTypeDataSource) =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: CompositePlaceTypeDataSource(
                            memorySource, localSource, remoteSource).also { INSTANCE = it }
                }
    }
}