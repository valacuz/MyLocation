package com.example.valacuz.mylocations.data.repository.realm

import android.annotation.SuppressLint
import android.content.Context
import android.preference.PreferenceManager
import com.example.valacuz.mylocations.data.PlaceType
import com.example.valacuz.mylocations.data.repository.PlaceTypeDataSource
import com.example.valacuz.mylocations.extension.toPlaceType
import com.example.valacuz.mylocations.extension.toRealmPlaceType
import io.reactivex.Flowable
import io.realm.Realm

class RealmPlaceTypeDataSource private constructor(
        private val context: Context) : PlaceTypeDataSource {

    override fun getAllTypes(): Flowable<List<PlaceType>> {
        return Flowable.just(
                Realm.getDefaultInstance().use { realm ->
                    realm.where(RealmPlaceType::class.java)
                            .findAll()
                            .map { items ->
                                items.toPlaceType()
                            }
                })
    }

    override fun addTypes(types: List<PlaceType>) {
        Realm.getDefaultInstance().use { realm ->
            realm.beginTransaction()
            realm.insertOrUpdate(types.map { type: PlaceType -> type.toRealmPlaceType() })
            realm.commitTransaction()
            // Update ticks
            updateTicks()
        }
    }

    override fun clearTypes() {
        Realm.getDefaultInstance().use { realm ->
            val items = realm.where(RealmPlaceType::class.java).findAll()
            if (items.isNotEmpty()) {
                realm.beginTransaction()
                val isDeleted = items.deleteAllFromRealm()
                if (isDeleted) {
                    realm.commitTransaction()
                } else {
                    realm.cancelTransaction()
                    throw Throwable("Cannot delete one or more place types. No place type(s) found.")
                }
            }
        }
    }

    override fun isDirty(): Boolean {
        val ticks = PreferenceManager
                .getDefaultSharedPreferences(context)
                .getLong(KEY_PLACE_TYPE_TICKS, 0)
        return System.currentTimeMillis() - ticks > (60 * 60 * 1_000)   // 1 Hour
    }

    private fun updateTicks() {
        PreferenceManager
                .getDefaultSharedPreferences(context)
                .edit()
                .putLong(KEY_PLACE_TYPE_TICKS, System.currentTimeMillis())
                .apply()
    }

    companion object {

        private const val KEY_PLACE_TYPE_TICKS = "REALM_PLACE_TYPE_TICKS"

        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: RealmPlaceTypeDataSource? = null

        fun getInstance(context: Context) =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: RealmPlaceTypeDataSource(context.applicationContext)
                            .also { INSTANCE = it }
                }
    }
}