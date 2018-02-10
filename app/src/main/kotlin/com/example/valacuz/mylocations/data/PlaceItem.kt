package com.example.valacuz.mylocations.data

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "tbl_place")
data class PlaceItem constructor(@ColumnInfo(name = "place_name") var name: String?,
                                 @ColumnInfo(name = "latitude") var latitude: Double,
                                 @ColumnInfo(name = "longitude") var longitude: Double,
                                 @ColumnInfo(name = "place_type") var type: Int,
                                 @ColumnInfo(name = "starred") var isStarred: Boolean = false,
                                 @ColumnInfo(name = "place_id") @PrimaryKey(autoGenerate = false)
                                 var id: String = UUID.randomUUID().toString()) {

    fun isEmpty(): Boolean = name.isNullOrEmpty()
}