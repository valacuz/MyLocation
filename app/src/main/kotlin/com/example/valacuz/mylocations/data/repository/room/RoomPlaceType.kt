package com.example.valacuz.mylocations.data.repository.room

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = RoomPlaceType.TABLE_NAME)
class RoomPlaceType(@ColumnInfo(name = COL_ID) @PrimaryKey(autoGenerate = false) var id: Int,
                    @ColumnInfo(name = COL_NAME) var name: String) {

    companion object {
        const val TABLE_NAME = "tbl_place_type"
        const val COL_ID = "type_id"
        const val COL_NAME = "type_name"
    }
}