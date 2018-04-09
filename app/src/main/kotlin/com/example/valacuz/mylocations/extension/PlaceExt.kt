package com.example.valacuz.mylocations.extension

import com.example.valacuz.mylocations.data.PlaceItem
import com.example.valacuz.mylocations.data.repository.remote.PlaceResponse
import com.example.valacuz.mylocations.data.repository.room.RoomPlaceItem

fun PlaceResponse.toPlaceItem(): PlaceItem {
    return PlaceItem(name, latitude, longitude, type, starred, id)
}

fun RoomPlaceItem.toPlaceItem(): PlaceItem {
    return PlaceItem(name, latitude, longitude, type, isStarred, id)
}

fun PlaceItem.toRoomPlace(): RoomPlaceItem {
    return RoomPlaceItem(name, latitude, longitude, type, isStarred, id)
}