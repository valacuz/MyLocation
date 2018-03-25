package com.example.valacuz.mylocations.data.repository.room

import android.annotation.SuppressLint
import android.content.Context
import android.preference.PreferenceManager
import com.example.valacuz.mylocations.data.PlaceItem
import com.example.valacuz.mylocations.data.repository.PlaceDataSource
import io.reactivex.Completable
import io.reactivex.Flowable

class RoomPlaceDataSource private constructor(
        private val placeDao: PlaceDao,
        private val context: Context) : PlaceDataSource {

    override fun getAllPlaces(): Flowable<List<PlaceItem>> = placeDao.getAllPlaces()

    override fun getById(placeId: String): Flowable<PlaceItem> = placeDao.getById(placeId)

    override fun addPlace(place: PlaceItem): Completable {
        return if (placeDao.addPlace(place) > 0)
            Completable.complete()
        else
            Completable.error(Throwable("Cannot add new place."))
    }

    override fun addPlaces(places: List<PlaceItem>): Completable {
        // Clear old one
        return clearPlaces().andThen(
                Completable.fromAction({
                    // Add places
                    placeDao.addPlaces(places)
                    // Update ticks
                    PreferenceManager
                            .getDefaultSharedPreferences(context)
                            .edit()
                            .putLong(KEY_PLACE_TICKS, System.currentTimeMillis())
                            .apply()
                    // Return as complete
                    Completable.complete()
                }))
    }

    override fun updatePlace(place: PlaceItem): Completable {
        return if (placeDao.updatePlace(place) > 0)
            Completable.complete()
        else
            Completable.error(Throwable("Cannot update place."))
    }

    override fun deletePlace(place: PlaceItem): Completable {
        // Here is observe thread
        // (exception will be thrown because room database doesn't allow to working on UI thread)

        return Completable.fromAction({
            // Here is subscribe thread
            if (placeDao.deletePlace(place) > 0)
                Completable.complete()
            else
                Completable.error(Throwable("Place not found."))
        })
    }

    override fun clearPlaces(): Completable {
        return Completable.fromAction({
            if (placeDao.clearPlaces() > 0)
                Completable.complete()
            else
                Completable.error(Throwable("Cannot remove one or more place."))
        })
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

        fun getInstance(placeDao: PlaceDao, context: Context) =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: RoomPlaceDataSource(placeDao, context.applicationContext)
                            .also { INSTANCE = it }
                }
    }
}