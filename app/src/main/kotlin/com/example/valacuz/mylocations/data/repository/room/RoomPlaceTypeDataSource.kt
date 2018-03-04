package com.example.valacuz.mylocations.data.repository.room

import android.annotation.SuppressLint
import android.content.Context
import com.example.valacuz.mylocations.data.PlaceType
import com.example.valacuz.mylocations.data.repository.PlaceTypeDataSource
import io.reactivex.Flowable

class RoomPlaceTypeDataSource private constructor(val context: Context) : PlaceTypeDataSource {

    override fun getAllTypes(): Flowable<List<PlaceType>> =
            AppDatabase.getInstance(context).run {
                placeTypeDao().getAllTypes().also { close() }
            }

    override fun addTypes(types: List<PlaceType>) =
            AppDatabase.getInstance(context).run {
                placeTypeDao().addPlaceTypes(types).also { close() }
            }

    companion object {

        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: RoomPlaceTypeDataSource? = null

        fun getInstance(context: Context) =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: RoomPlaceTypeDataSource(context.applicationContext)
                            .also { INSTANCE = it }
                }
    }
}