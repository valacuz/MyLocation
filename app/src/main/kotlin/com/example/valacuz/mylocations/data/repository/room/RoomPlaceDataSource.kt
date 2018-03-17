package com.example.valacuz.mylocations.data.repository.room

import android.annotation.SuppressLint
import android.content.Context
import android.preference.PreferenceManager
import com.example.valacuz.mylocations.data.PlaceItem
import com.example.valacuz.mylocations.data.repository.PlaceDataSource
import io.reactivex.Flowable

class RoomPlaceDataSource private constructor(
        private val placeDao: PlaceDao,
        private val context: Context) : PlaceDataSource {

    override fun getAllPlaces(): Flowable<List<PlaceItem>> = placeDao.getAllPlaces()

    override fun getById(placeId: String): Flowable<PlaceItem> = placeDao.getById(placeId)

    override fun addPlace(place: PlaceItem) = placeDao.addPlace(place)

    override fun addPlaces(places: List<PlaceItem>) =
            placeDao.addPlaces(places)
                    .run {
                        PreferenceManager
                                .getDefaultSharedPreferences(context)
                                .edit()
                                .putLong(KEY_PLACE_TICKS, System.currentTimeMillis())
                                .apply()
                    }

    override fun updatePlace(place: PlaceItem) = placeDao.updatePlace(place)

    override fun deletePlace(place: PlaceItem) = placeDao.deletePlace(place)

    override fun clearPlaces() = placeDao.clearPlaces()

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

        fun getInstance(placeDao: PlaceDao, context: Context) =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: RoomPlaceDataSource(placeDao, context.applicationContext)
                            .also { INSTANCE = it }
                }
    }
}