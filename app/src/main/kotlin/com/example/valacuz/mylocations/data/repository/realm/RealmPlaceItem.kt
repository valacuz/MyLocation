package com.example.valacuz.mylocations.data.repository.realm

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import io.realm.annotations.RealmField

@RealmClass(name = "tbl_place")
open class RealmPlaceItem(
        @RealmField(name = "place_name") var name: String?,
        @RealmField(name = "latitude") var latitude: Double,
        @RealmField(name = "longitude") var longitude: Double,
        @RealmField(name = "place_type") var type: Int,
        @RealmField(name = "starred") var isStarred: Boolean = false,
        @RealmField(name = "place_id") @PrimaryKey var id: String)
    : RealmObject() {

    constructor() : this(null, 0.0, 0.0, 0, false, "")
}