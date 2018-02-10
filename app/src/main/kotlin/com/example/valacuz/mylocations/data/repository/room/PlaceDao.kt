package com.example.valacuz.mylocations.data.repository.room

import android.arch.persistence.room.*
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import com.example.valacuz.mylocations.data.PlaceDataSource
import com.example.valacuz.mylocations.data.PlaceItem
import com.example.valacuz.mylocations.data.PlaceType
import io.reactivex.Flowable

@Dao
interface PlaceDao : PlaceDataSource {

    @Query("SELECT * FROM tbl_place")
    override fun getAllPlaces(): Flowable<List<PlaceItem>>

    @Query("SELECT * FROM tbl_place_type")
    override fun getAllTypes(): Flowable<List<PlaceType>>

    @Query("SELECT * FROM tbl_place WHERE place_id = :placeId")
    override fun getById(placeId: String): Flowable<PlaceItem>

    @Insert(onConflict = REPLACE)
    override fun addPlace(placeItem: PlaceItem)

    @Insert(onConflict = REPLACE)
    fun addPlaceTypes(types: List<PlaceType>)

    @Update(onConflict = REPLACE)
    override fun updatePlace(placeItem: PlaceItem)

    @Delete
    override fun deletePlace(placeItem: PlaceItem)

    @Query("DELETE FROM tbl_place")
    override fun clearPlaces()
}