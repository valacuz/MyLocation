package com.example.valacuz.mylocations.data

import java.util.UUID

data class PlaceItem constructor(val name: String,
                                 val latitude: Double,
                                 val longitude: Double,
                                 val type: Int,
                                 val isStarred: Boolean = false,
                                 val id: String = UUID.randomUUID().toString())