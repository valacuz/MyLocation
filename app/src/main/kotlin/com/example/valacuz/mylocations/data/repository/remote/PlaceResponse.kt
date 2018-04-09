package com.example.valacuz.mylocations.data.repository.remote

import com.google.gson.annotations.SerializedName

class PlaceResponse(
        @SerializedName("place_id") var id: String,
        @SerializedName("place_name") var name: String,
        @SerializedName("place_type") var type: Int,
        @SerializedName("latitude") var latitude: Double,
        @SerializedName("longitude") var longitude: Double,
        @SerializedName("starred") var starred: Boolean)