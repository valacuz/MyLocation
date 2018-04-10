package com.example.valacuz.mylocations.extension

import com.example.valacuz.mylocations.data.PlaceItem
import com.example.valacuz.mylocations.data.PlaceType
import com.example.valacuz.mylocations.data.repository.remote.PlaceResponse
import com.example.valacuz.mylocations.data.repository.remote.PlaceTypeResponse
import com.example.valacuz.mylocations.data.repository.room.RoomPlaceItem
import com.example.valacuz.mylocations.data.repository.room.RoomPlaceType

fun PlaceResponse.toPlaceItem(): PlaceItem {
    return PlaceItem(name, latitude, longitude, type, starred, id)
}

fun RoomPlaceItem.toPlaceItem(): PlaceItem {
    return PlaceItem(name, latitude, longitude, type, isStarred, id)
}

fun PlaceItem.toRoomPlace(): RoomPlaceItem {
    return RoomPlaceItem(name, latitude, longitude, type, isStarred, id)
}

fun PlaceTypeResponse.toPlaceType(): PlaceType {
    return PlaceType(id, name)
}

fun RoomPlaceType.toPlaceType(): PlaceType {
    return PlaceType(id, name)
}

fun PlaceType.toRoomPlaceType(): RoomPlaceType {
    return RoomPlaceType(id, name)
}