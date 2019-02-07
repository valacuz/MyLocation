package com.example.valacuz.mylocations.data.repository.room

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = RoomPlaceItem.TABLE_NAME)
class RoomPlaceItem(
        @ColumnInfo(name = COL_NAME) var name: String?,
        @ColumnInfo(name = COL_LATITUDE) var latitude: Double,
        @ColumnInfo(name = COL_LONGITUDE) var longitude: Double,
        @ColumnInfo(name = COL_TYPE) var type: Int,
        @ColumnInfo(name = COL_STARRED) var isStarred: Boolean = false,
        @ColumnInfo(name = COL_PICTURE_PATH) var picturePath: String?,
        @ColumnInfo(name = COL_ID) @PrimaryKey(autoGenerate = false) var id: String) {


    companion object {
        const val TABLE_NAME = "tbl_place"
        const val COL_NAME = "place_name"
        const val COL_LATITUDE = "latitude"
        const val COL_LONGITUDE = "longitude"
        const val COL_TYPE = "place_type"
        const val COL_STARRED = "starred"
        const val COL_PICTURE_PATH = "picture_path"
        const val COL_ID = "place_id"
    }
}