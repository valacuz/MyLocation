package com.example.valacuz.mylocations.data.repository.room

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import io.reactivex.Flowable

@Dao
interface PlaceTypeDao {

    @Query(value = "SELECT * FROM ${RoomPlaceType.TABLE_NAME}")
    fun getAllTypes(): Flowable<List<RoomPlaceType>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addPlaceTypes(types: List<RoomPlaceType>)

    /**
     * Deletes all places in database.
     *
     * @return A number of places deleted.
     */
    @Query("DELETE FROM ${RoomPlaceType.TABLE_NAME}")
    fun clearPlaceTypes(): Int
}