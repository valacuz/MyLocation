package com.example.valacuz.mylocations.data.repository.memory

import com.example.valacuz.mylocations.data.PlaceType
import com.example.valacuz.mylocations.data.repository.PlaceTypeDataSource
import io.reactivex.Completable
import io.reactivex.Flowable

open class MemoryPlaceTypeDataSource internal constructor() : PlaceTypeDataSource {

    private val items: MutableList<PlaceType> = mutableListOf()

    private var ticks: Long = 0

    override fun getAllTypes(): Flowable<List<PlaceType>> = Flowable.fromArray(items)

    override fun addTypes(types: List<PlaceType>): Completable {
        return clearTypes()
                .andThen(Completable.fromAction({
                    items.addAll(types)
                    ticks = System.currentTimeMillis()
                    Completable.complete()
                }))
    }

    override fun clearTypes(): Completable {
        return Completable.fromAction({
            items.clear()
            if (items.size == 0)
                Completable.complete()
            else
                Completable.error(Throwable("Cannot delete one or more place types"))
        })
    }

    // Check is data is dirty (default 5 minutes or when data is empty)
    override fun isDirty() =
            System.currentTimeMillis() - ticks > (5 * 60 * 1_000) || items.isEmpty()

    companion object {

        @Volatile
        private var INSTANCE: MemoryPlaceTypeDataSource? = null

        fun getInstance(): MemoryPlaceTypeDataSource =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: MemoryPlaceTypeDataSource().also { INSTANCE = it }
                }
    }
}