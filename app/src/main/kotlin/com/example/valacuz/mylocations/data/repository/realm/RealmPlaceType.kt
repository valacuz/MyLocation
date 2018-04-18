package com.example.valacuz.mylocations.data.repository.realm

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import io.realm.annotations.RealmField

@RealmClass(name = "tbl_place_type")
open class RealmPlaceType(
        @RealmField(name = "type_id") @PrimaryKey var id: Int,
        @RealmField(name = "type_name") var name: String)
    : RealmObject() {

    constructor() : this(0, "")
}