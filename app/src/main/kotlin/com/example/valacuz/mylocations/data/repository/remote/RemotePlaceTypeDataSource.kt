package com.example.valacuz.mylocations.data.repository.remote

import com.example.valacuz.mylocations.data.PlaceType
import com.example.valacuz.mylocations.data.repository.PlaceTypeDataSource
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable

class RemotePlaceTypeDataSource private constructor(
        private val samplePlaceService: SamplePlaceService) : PlaceTypeDataSource {

    override fun getAllTypes(): Flowable<List<PlaceType>> =
            samplePlaceService
                    .getPlaceTypes()
                    .map({ responseTypes ->
                        responseTypes.map {
                            PlaceType(it.id, it.name)
                        }
                    }).toFlowable(BackpressureStrategy.LATEST)

    override fun addTypes(types: List<PlaceType>) {
        // Operation not support on remote.
    }

    override fun isDirty() = false // Never!

    companion object {

        @Volatile
        private var INSTANCE: RemotePlaceTypeDataSource? = null

        fun getInstance(samplePlaceService: SamplePlaceService) =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: RemotePlaceTypeDataSource(samplePlaceService)
                            .also { INSTANCE = it }
                }
    }
}