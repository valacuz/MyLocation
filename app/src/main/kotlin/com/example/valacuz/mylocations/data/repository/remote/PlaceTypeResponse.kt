package com.example.valacuz.mylocations.data.repository.remote

import com.google.gson.annotations.SerializedName

data class PlaceTypeResponse(
        @SerializedName("type_id") val id: Int,
        @SerializedName("type_name") val name: String)