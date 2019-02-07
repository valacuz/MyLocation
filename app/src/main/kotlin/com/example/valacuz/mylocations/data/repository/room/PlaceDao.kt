package com.example.valacuz.mylocations.data.repository.room

import android.arch.persistence.room.*
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import io.reactivex.Flowable

@Dao
interface PlaceDao {

    /**
     * Retrieve all places from database.
     *
     * @return A list of [RoomPlaceItem].
     */
    @Query("SELECT * FROM ${RoomPlaceItem.TABLE_NAME}")
    fun getAllPlaces(): Flowable<List<RoomPlaceItem>>

    /**
     * Retrieve place from given id.
     *
     * @param placeId Place id to be retrieved.
     * @return A [RoomPlaceItem]
     */
    @Query("SELECT * FROM ${RoomPlaceItem.TABLE_NAME} WHERE ${RoomPlaceItem.COL_ID} = :placeId")
    fun getById(placeId: String): Flowable<RoomPlaceItem>

    /**
     * Add a new place.
     *
     * @param placeItem A place to be added.
     */
    @Insert(onConflict = REPLACE)
    fun addPlace(placeItem: RoomPlaceItem): Long

    /**
     * Add a list of places.
     *
     * @param places A list of places to be added.
     */
    @Insert(onConflict = REPLACE)
    fun addPlaces(places: List<RoomPlaceItem>)

    /**
     * Update place from given place. (the place is identified by its id)
     *
     * @param placeItem Place to be updated.
     */
    @Update(onConflict = REPLACE)
    fun updatePlace(placeItem: RoomPlaceItem): Int

    /**
     * Delete places from given place.
     *
     * @param placeItem Place to be deleted.
     * @return A number of place deleted, This should be [1]
     */
    @Delete
    fun deletePlace(placeItem: RoomPlaceItem): Int

    /**
     * Deletes all places in database.
     *
     * @return A number of places deleted.
     */
    @Query("DELETE FROM ${RoomPlaceItem.TABLE_NAME}")
    fun clearPlaces(): Int
}