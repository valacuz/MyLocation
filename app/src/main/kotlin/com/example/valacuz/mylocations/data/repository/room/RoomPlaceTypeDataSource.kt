package com.example.valacuz.mylocations.data.repository.room

import android.annotation.SuppressLint
import android.content.Context
import android.preference.PreferenceManager
import com.example.valacuz.mylocations.data.PlaceType
import com.example.valacuz.mylocations.data.repository.PlaceTypeDataSource
import io.reactivex.Completable
import io.reactivex.Flowable

class RoomPlaceTypeDataSource private constructor(
        private val placeTypeDao: PlaceTypeDao,
        private val context: Context) : PlaceTypeDataSource {

    override fun getAllTypes(): Flowable<List<PlaceType>> = placeTypeDao.getAllTypes()

    override fun addTypes(types: List<PlaceType>): Completable {
        // Clear old one
        return clearTypes()
                .andThen(Completable.fromAction({
                    // Add new place types
                    placeTypeDao.addPlaceTypes(types)
                    // Update ticks
                    PreferenceManager
                            .getDefaultSharedPreferences(context)
                            .edit()
                            .putLong(KEY_PLACE_TYPE_TICKS, System.currentTimeMillis())
                            .apply()
                    // Return as complete
                    Completable.complete()
                }))
    }

    override fun clearTypes(): Completable {
        return Completable.fromAction({
            if (placeTypeDao.clearPlaceTypes() > 0)
                Completable.complete()
            else
                Completable.error(Throwable("Cannot delete one or more place types"))
        })
    }

    override fun isDirty(): Boolean {
        val ticks = PreferenceManager
                .getDefaultSharedPreferences(context)
                .getLong(KEY_PLACE_TYPE_TICKS, 0)
        return System.currentTimeMillis() - ticks > (60 * 60 * 1_000) // 1 Hour
    }

    companion object {

        private const val KEY_PLACE_TYPE_TICKS = "ROOM_PLACE_TYPE_TICKS"

        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: RoomPlaceTypeDataSource? = null

        fun getInstance(placeTypeDao: PlaceTypeDao, context: Context) =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: RoomPlaceTypeDataSource(placeTypeDao, context.applicationContext)
                            .also { INSTANCE = it }
                }
    }
}