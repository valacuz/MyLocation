package com.example.valacuz.mylocations.data.repository.room

import android.arch.persistence.room.*
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import com.example.valacuz.mylocations.data.PlaceItem
import io.reactivex.Flowable

@Dao
interface PlaceDao {

    /**
     * Retrieve all places from database.
     *
     * @return A list of [PlaceItem].
     */
    @Query("SELECT * FROM tbl_place")
    fun getAllPlaces(): Flowable<List<PlaceItem>>

    /**
     * Retrieve place from given id.
     *
     * @param placeId Place id to be retrieved.
     * @return A [PlaceItem]
     */
    @Query("SELECT * FROM tbl_place WHERE place_id = :placeId")
    fun getById(placeId: String): Flowable<PlaceItem>

    /**
     * Add a new place.
     *
     * @param placeItem A place to be added.
     */
    @Insert(onConflict = REPLACE)
    fun addPlace(placeItem: PlaceItem): Long

    /**
     * Add a list of places.
     *
     * @param places A list of places to be added.
     */
    @Insert(onConflict = REPLACE)
    fun addPlaces(places: List<PlaceItem>)

    /**
     * Update place from given place. (the place is identified by its id)
     *
     * @param placeItem Place to be updated.
     */
    @Update(onConflict = REPLACE)
    fun updatePlace(placeItem: PlaceItem): Int

    /**
     * Delete places from given place.
     *
     * @param placeItem Place to be deleted.
     * @return A number of place deleted, This should be [1]
     */
    @Delete
    fun deletePlace(placeItem: PlaceItem): Int

    /**
     * Deletes all places in database.
     *
     * @return A number of places deleted.
     */
    @Query("DELETE FROM tbl_place")
    fun clearPlaces(): Int
}