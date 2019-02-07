package com.example.valacuz.mylocations.data.repository.realm

import android.annotation.SuppressLint
import android.content.Context
import android.preference.PreferenceManager
import com.example.valacuz.mylocations.data.PlaceItem
import com.example.valacuz.mylocations.data.repository.PlaceDataSource
import com.example.valacuz.mylocations.extension.toPlaceItem
import com.example.valacuz.mylocations.extension.toRealmPlace
import io.reactivex.Flowable
import io.realm.Realm

class RealmPlaceDataSource private constructor(
        private val context: Context) : PlaceDataSource {

    override fun getAllPlaces(): Flowable<List<PlaceItem>> {
        return Flowable.defer {
            Realm.getDefaultInstance().use { realm ->
                val items = realm.where(RealmPlaceItem::class.java)
                        .findAll()

                if (items != null) {
                    Flowable.just(items.map { it.toPlaceItem() })
                } else {
                    Flowable.empty()
                }
            }
        }
    }

    override fun getById(placeId: String): Flowable<PlaceItem> {
        return Flowable.defer {
            Realm.getDefaultInstance().use { realm ->
                val item = realm.where(RealmPlaceItem::class.java)
                        .equalTo(RealmPlaceItemFields.ID, placeId)
                        .findFirst()

                if (item != null) {
                    Flowable.just(item.toPlaceItem())
                } else {
                    Flowable.empty()
                }
            }
        }
    }

    override fun addPlace(place: PlaceItem) {
        Realm.getDefaultInstance().use {
            it.beginTransaction()
            it.insertOrUpdate(place.toRealmPlace())
            it.commitTransaction()
        }
    }

    override fun addPlaces(places: List<PlaceItem>) {
        Realm.getDefaultInstance().use { realm ->
            realm.beginTransaction()
            realm.insertOrUpdate(places.map { place -> place.toRealmPlace() })
            realm.commitTransaction()
            // Update ticks
            updateTicks()
        }
    }

    override fun updatePlace(place: PlaceItem) {
        Realm.getDefaultInstance().use {
            it.beginTransaction()
            it.insertOrUpdate(place.toRealmPlace())
            it.commitTransaction()
        }
    }

    override fun deletePlace(place: PlaceItem) {
        Realm.getDefaultInstance().use { realm ->
            // The field name IS NOT match to the name we defined in @RealmField
            val items = realm.where(RealmPlaceItem::class.java)
                    .equalTo(RealmPlaceItemFields.ID, place.id)
                    .findAll()

            if (items.isNotEmpty()) {
                realm.beginTransaction()
                val canDelete = items.deleteAllFromRealm()

                if (canDelete) {
                    realm.commitTransaction()
                } else {
                    realm.cancelTransaction()
                    throw Throwable("Cannot delete one or more place(s).")
                }
            } else {
                throw Throwable("Cannot delete one or more place(s). Place(s) not found.")
            }
        }
    }

    override fun clearPlaces() {
        Realm.getDefaultInstance().use { realm ->
            val items = realm.where(RealmPlaceItem::class.java).findAll()

            if (items.isNotEmpty()) {
                realm.beginTransaction()
                if (items.deleteAllFromRealm()) {
                    realm.commitTransaction()
                } else {
                    realm.cancelTransaction()
                    throw Throwable("Cannot delete one or more place(s).")
                }
            }
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

        private const val KEY_PLACE_TICKS = "REALM_PLACE_TICKS"

        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: RealmPlaceDataSource? = null

        fun getInstance(context: Context) =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: RealmPlaceDataSource(context.applicationContext)
                            .also { INSTANCE = it }
                }
    }
}