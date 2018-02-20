package com.example.valacuz.mylocations.data.repository.room

import android.annotation.SuppressLint
import android.content.Context
import com.example.valacuz.mylocations.data.PlaceDataSource
import com.example.valacuz.mylocations.data.PlaceItem
import com.example.valacuz.mylocations.data.PlaceType
import io.reactivex.Flowable

class LocalPlaceDataSource private constructor(val context: Context) : PlaceDataSource {

    override fun getAllPlaces(): Flowable<List<PlaceItem>> {
        val appDatabase = AppDatabase.getInstance(context)
        val places = appDatabase
                .placeItemDao()
                .getAllPlaces()
        appDatabase.close()
        return places
    }

    override fun getAllTypes(): Flowable<List<PlaceType>> {
        val appDatabase = AppDatabase.getInstance(context)
        val types = appDatabase
                .placeItemDao()
                .getAllTypes()
        appDatabase.close()
        return types
    }

    override fun getById(placeId: String): Flowable<PlaceItem> {
        val appDatabase = AppDatabase.getInstance(context)
        val placeItem = appDatabase
                .placeItemDao()
                .getById(placeId)
        appDatabase.close()
        return placeItem
    }

    override fun addPlace(placeItem: PlaceItem) {
        val appDatabase = AppDatabase.getInstance(context)
        appDatabase
                .placeItemDao()
                .addPlace(placeItem)
        appDatabase.close()
    }

    override fun updatePlace(placeItem: PlaceItem) {
        val appDatabase = AppDatabase.getInstance(context)
        appDatabase
                .placeItemDao()
                .updatePlace(placeItem)
        appDatabase.close()
    }

    override fun deletePlace(placeItem: PlaceItem) {
        val appDatabase = AppDatabase.getInstance(context)
        appDatabase
                .placeItemDao()
                .deletePlace(placeItem)
        appDatabase.close()
    }

    override fun clearPlaces() {
        val appDatabase = AppDatabase.getInstance(context)
        appDatabase
                .placeItemDao()
                .clearPlaces()
        appDatabase.close()
    }

    companion object {

        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: LocalPlaceDataSource? = null

        fun getInstance(context: Context) =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: LocalPlaceDataSource(context.applicationContext)
                            .also { INSTANCE = it }
                }
    }
}