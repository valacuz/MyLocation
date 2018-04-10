package com.example.valacuz.mylocations.data.repository.remote

import com.example.valacuz.mylocations.data.PlaceType
import com.example.valacuz.mylocations.data.repository.PlaceTypeDataSource
import com.example.valacuz.mylocations.extension.toPlaceType
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Flowable

class RemotePlaceTypeDataSource private constructor(
        private val samplePlaceService: SamplePlaceService) : PlaceTypeDataSource {

    override fun getAllTypes(): Flowable<List<PlaceType>> =
            samplePlaceService
                    .getPlaceTypes()
                    .map { items ->
                        items.map { it.toPlaceType() }
                    }.toFlowable(BackpressureStrategy.LATEST)

    // Operation not support on remote.
    override fun addTypes(types: List<PlaceType>): Completable = Completable.complete()

    override fun clearTypes(): Completable = samplePlaceService.clearPlaceTypes()

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