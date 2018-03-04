package com.example.valacuz.mylocations.data.repository.room

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.example.valacuz.mylocations.data.PlaceType
import io.reactivex.Flowable

@Dao
interface PlaceTypeDao {

    @Query(value = "SELECT * FROM tbl_place_type")
    fun getAllTypes(): Flowable<List<PlaceType>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addPlaceTypes(types: List<PlaceType>)
}