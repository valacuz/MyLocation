package com.example.valacuz.mylocations.data.repository.room

import android.annotation.SuppressLint
import android.content.Context
import android.preference.PreferenceManager
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
                placeTypeDao()
                        .addPlaceTypes(types)
                        .also { close() }
                        .run {
                            PreferenceManager
                                    .getDefaultSharedPreferences(context)
                                    .edit()
                                    .putLong(KEY_PLACE_TYPE_TICKS, System.currentTimeMillis())
                                    .apply()
                        }
            }

    override fun isDirty(): Boolean {
        val ticks = PreferenceManager
                .getDefaultSharedPreferences(context)
                .getLong(KEY_PLACE_TYPE_TICKS, 0)
        return System.currentTimeMillis() - ticks > (10 * 60 * 1_000) // 10 Minutes
    }

    companion object {

        private const val KEY_PLACE_TYPE_TICKS = "ROOM_PLACE_TYPE_TICKS"

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