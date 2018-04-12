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
        Realm.getDefaultInstance().use {
            return Flowable.just(it.where(RealmPlaceItem::class.java)
                    .findAll()
                    .map { items ->
                        items.toPlaceItem()
                    })
        }
    }

    override fun getById(placeId: String): Flowable<PlaceItem> {
        Realm.getDefaultInstance().use {
            return Flowable.defer {
                val realmPlace = it.where(RealmPlaceItem::class.java)
                        .equalTo("place_id", placeId)
                        .findFirst()

                if (realmPlace != null) {
                    Flowable.just(realmPlace.toPlaceItem())
                } else {
                    Flowable.empty()
                }
            }
        }
    }

    override fun addPlace(place: PlaceItem): Completable {
        Realm.getDefaultInstance().use {
            return Completable.defer {
                it.insertOrUpdate(place.toRealmPlace())
                Completable.complete()
            }
        }
    }

    override fun addPlaces(places: List<PlaceItem>): Completable {
        Realm.getDefaultInstance().use {
            return Completable.defer {
                it.insertOrUpdate(places.map { place: PlaceItem ->
                    place.toRealmPlace()
                })
                // Update ticks
                updateTicks()
                // Return as complete
                Completable.complete()
            }
        }
    }

    override fun updatePlace(place: PlaceItem): Completable {
        // Do the same as addPlace
        Realm.getDefaultInstance().use {
            return Completable.defer {
                it.insertOrUpdate(place.toRealmPlace())
                Completable.complete()
            }
        }
    }

    override fun deletePlace(place: PlaceItem): Completable {
        Realm.getDefaultInstance().use {
            return Completable.defer {
                val items = it.where(RealmPlaceItem::class.java)
                        .equalTo("place_id", place.id)
                        .findAll()
                if (items.isNotEmpty() && items.deleteAllFromRealm()) {
                    Completable.complete()
                } else {
                    Completable.error(Throwable("Cannot delete one or more places. No place(s) found."))
                }
            }
        }
    }

    override fun clearPlaces(): Completable {
        Realm.getDefaultInstance().use {
            return Completable.defer {
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