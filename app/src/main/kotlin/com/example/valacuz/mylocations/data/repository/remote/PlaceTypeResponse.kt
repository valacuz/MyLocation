package com.example.valacuz.mylocations.data.repository.remote

import com.google.gson.annotations.SerializedName

data class PlaceTypeResponse(
        @SerializedName("id") val id: Int,
        @SerializedName("name") val name: String)