package com.example.valacuz.mylocations.data.repository.room

import android.annotation.SuppressLint
import android.content.Context
import android.preference.PreferenceManager
import com.example.valacuz.mylocations.data.PlaceItem
import com.example.valacuz.mylocations.data.repository.PlaceDataSource
import com.example.valacuz.mylocations.extension.toPlaceItem
import com.example.valacuz.mylocations.extension.toRoomPlace
import io.reactivex.Completable
import io.reactivex.Flowable

class RoomPlaceDataSource private constructor(
        private val placeDao: PlaceDao,
        private val context: Context) : PlaceDataSource {

    override fun getAllPlaces(): Flowable<List<PlaceItem>> {
        return placeDao.getAllPlaces()
                .map { items: List<RoomPlaceItem> ->
                    items.map { it.toPlaceItem() }
                }
    }

    override fun getById(placeId: String): Flowable<PlaceItem> {
        return placeDao.getById(placeId)
                .map { it.toPlaceItem() }
    }

    override fun addPlace(place: PlaceItem) {
        if (placeDao.addPlace(place.toRoomPlace()) <= 0) {
            throw Throwable("Cannot add new place.")
        }
    }

    override fun addPlaces(places: List<PlaceItem>) {
        // Add places
        val roomPlaces = places.map { it.toRoomPlace() }
        placeDao.addPlaces(roomPlaces)
        // Update ticks
        updateTicks()
        // Return as complete
        Completable.complete()
    }

    override fun updatePlace(place: PlaceItem) {
        if (placeDao.updatePlace(place.toRoomPlace()) <= 0) {
            throw Throwable("Cannot update place.")
        }
    }

    override fun deletePlace(place: PlaceItem) {
        if (placeDao.deletePlace(place.toRoomPlace()) <= 0) {
            throw Throwable("Cannot delete one or more place(s). Place not found.")
        }
    }

    override fun clearPlaces() {
        if (placeDao.clearPlaces() < 0) {
            throw Throwable("Cannot delete one or more place(s).")
        }
    }

    override fun isDirty(): Boolean {
        val ticks = PreferenceManager
                .getDefaultSharedPreferences(context)
                .getLong(KEY_PLACE_TICKS, 0)
        return System.currentTimeMillis() - ticks > (60 * 60 * 1_000)   // 1 Hour
    }

    private fun updateTicks() {
        PreferenceManager
                .getDefaultSharedPreferences(context)
                .edit()
                .putLong(KEY_PLACE_TICKS, System.currentTimeMillis())
                .apply()
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