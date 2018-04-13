package com.example.valacuz.mylocations.data.repository.realm

import android.annotation.SuppressLint
import android.content.Context
import android.preference.PreferenceManager
import com.example.valacuz.mylocations.data.PlaceItem
import com.example.valacuz.mylocations.data.repository.PlaceDataSource
import com.example.valacuz.mylocations.extension.toPlaceItem
import com.example.valacuz.mylocations.extension.toRealmPlace
import io.reactivex.Completable
import io.reactivex.Flowable
import io.realm.Realm

class RealmPlaceDataSource private constructor(
        private val context: Context) : PlaceDataSource {

    override fun getAllPlaces(): Flowable<List<PlaceItem>> {
        return Flowable.just(
                Realm.getDefaultInstance().use {
                    it.where(RealmPlaceItem::class.java)
                            .findAll()
                            .map { items ->
                                items.toPlaceItem()
                            }
                })
    }

    override fun getById(placeId: String): Flowable<PlaceItem> {
        return Flowable.just(
                Realm.getDefaultInstance().use {
                    it.where(RealmPlaceItem::class.java)
                            .equalTo(RealmPlaceItemFields.ID, placeId)
                            .findFirst()
                            ?.toPlaceItem()
                })
    }

    override fun addPlace(place: PlaceItem): Completable {
        return Completable.defer {
            Realm.getDefaultInstance().use {
                it.beginTransaction()
                it.insertOrUpdate(place.toRealmPlace())
                it.commitTransaction()
                //
                Completable.complete()
            }
        }
    }

    override fun addPlaces(places: List<PlaceItem>): Completable {
        return Completable.defer {
            Realm.getDefaultInstance().use {
                it.beginTransaction()
                it.insertOrUpdate(places.map { place: PlaceItem ->
                    place.toRealmPlace()
                })
                it.commitTransaction()
                // Update ticks
                updateTicks()
                // Return as complete
                Completable.complete()
            }
        }
    }

    override fun updatePlace(place: PlaceItem): Completable {
        return Completable.defer {
            Realm.getDefaultInstance().use {
                it.beginTransaction()
                it.insertOrUpdate(place.toRealmPlace())
                it.commitTransaction()
                // Return as complete
                Completable.complete()
            }
        }
    }

    override fun deletePlace(place: PlaceItem): Completable {
        return Completable.defer {
            Realm.getDefaultInstance().use {
                it.beginTransaction()

                // The field name IS NOT match to the name we defined in @RealmField
                val items = it.where(RealmPlaceItem::class.java)
                        .equalTo(RealmPlaceItemFields.ID, place.id)
                        .findAll()
                if (items.isNotEmpty() && items.deleteAllFromRealm()) {
                    it.commitTransaction()
                    Completable.complete()
                } else {
                    it.cancelTransaction()
                    Completable.error(Throwable("Cannot delete one or more places. No place(s) found."))
                }
            }
        }
    }

    override fun clearPlaces(): Completable {
        return Completable.defer {
            Realm.getDefaultInstance().use {
                val items = it.where(RealmPlaceItem::class.java).findAll()
                if (items.isNotEmpty() && items.deleteAllFromRealm()) {
                    Completable.complete()
                } else {
                    Completable.error(Throwable("Cannot delete one or more places. No place(s) found."))
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