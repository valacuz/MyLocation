package com.example.valacuz.mylocations.extension

import com.example.valacuz.mylocations.data.PlaceItem
import com.example.valacuz.mylocations.data.PlaceType
import com.example.valacuz.mylocations.data.repository.realm.RealmPlaceItem
import com.example.valacuz.mylocations.data.repository.realm.RealmPlaceType
import com.example.valacuz.mylocations.data.repository.remote.PlaceResponse
import com.example.valacuz.mylocations.data.repository.remote.PlaceTypeResponse
import com.example.valacuz.mylocations.data.repository.room.RoomPlaceItem
import com.example.valacuz.mylocations.data.repository.room.RoomPlaceType

// == Place Item extensions

fun PlaceResponse.toPlaceItem(): PlaceItem {
    return PlaceItem(name, latitude, longitude, type, starred, id)
}

fun RoomPlaceItem.toPlaceItem(): PlaceItem {
    return PlaceItem(name, latitude, longitude, type, isStarred, id)
}

fun RealmPlaceItem.toPlaceItem(): PlaceItem {
    return PlaceItem(name, latitude, longitude, type, isStarred, id)
}

fun PlaceItem.toRoomPlace(): RoomPlaceItem {
    return RoomPlaceItem(name, latitude, longitude, type, isStarred, id)
}

fun PlaceItem.toRealmPlace(): RealmPlaceItem {
    return RealmPlaceItem(name, latitude, longitude, type, isStarred, id)
}

// == PlaceType extensions

fun PlaceTypeResponse.toPlaceType(): PlaceType {
    return PlaceType(id, name)
}

fun RoomPlaceType.toPlaceType(): PlaceType {
    return PlaceType(id, name)
}

fun RealmPlaceType.toPlaceType(): PlaceType {
    return PlaceType(id, name)
}

fun PlaceType.toRoomPlaceType(): RoomPlaceType {
    return RoomPlaceType(id, name)
}

fun PlaceType.toRealmPlaceType(): RealmPlaceType {
    return RealmPlaceType(id, name)
}