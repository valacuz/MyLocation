package com.example.valacuz.mylocations.data.repository.room

import android.annotation.SuppressLint
import android.content.Context
import android.preference.PreferenceManager
import com.example.valacuz.mylocations.data.PlaceItem
import com.example.valacuz.mylocations.data.repository.PlaceDataSource
import io.reactivex.Flowable

class RoomPlaceDataSource private constructor(val context: Context) : PlaceDataSource {

    override fun getAllPlaces(): Flowable<List<PlaceItem>> =
            AppDatabase.getInstance(context).run {
                placeItemDao().getAllPlaces().also { close() }
            }

    override fun getById(placeId: String): Flowable<PlaceItem> =
            AppDatabase.getInstance(context).run {
                placeItemDao().getById(placeId).also { close() }
            }

    override fun addPlace(place: PlaceItem) =
            AppDatabase.getInstance(context).run {
                placeItemDao().addPlace(place).also { close() }
            }

    override fun addPlaces(places: List<PlaceItem>) =
            AppDatabase.getInstance(context).run {
                placeItemDao().addPlaces(places)
                        .also { close() }
                        .run {
                            PreferenceManager
                                    .getDefaultSharedPreferences(context)
                                    .edit()
                                    .putLong(KEY_PLACE_TICKS, System.currentTimeMillis())
                                    .apply()
                        }
            }

    override fun updatePlace(place: PlaceItem) =
            AppDatabase.getInstance(context).run {
                placeItemDao().updatePlace(place).also { close() }
            }

    override fun deletePlace(place: PlaceItem) =
            AppDatabase.getInstance(context).run {
                placeItemDao().deletePlace(place).also { close() }
            }

    override fun clearPlaces() =
            AppDatabase.getInstance(context).run {
                placeItemDao().clearPlaces().also { close() }
            }

    override fun isDirty(): Boolean {
        val ticks = PreferenceManager
                .getDefaultSharedPreferences(context)
                .getLong(KEY_PLACE_TICKS, 0)
        return System.currentTimeMillis() - ticks > (60 * 60 * 1_000)   // 1 Hour
    }

    companion object {

        private const val KEY_PLACE_TICKS = "ROOM_PLACE_TICKS"

        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: RoomPlaceDataSource? = null

        fun getInstance(context: Context) =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: RoomPlaceDataSource(context.applicationContext)
                            .also { INSTANCE = it }
                }
    }
}