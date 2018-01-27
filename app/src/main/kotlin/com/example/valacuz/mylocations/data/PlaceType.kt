package com.example.valacuz.mylocations.data

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "tbl_place_type")
data class PlaceType(@ColumnInfo(name = "type_id") @PrimaryKey(autoGenerate = false) var id: Int,
                     @ColumnInfo(name = "type_name") var name: String) {

    override fun toString(): String = name
}