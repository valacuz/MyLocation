package com.example.valacuz.mylocations.data

import java.util.*

/**
 * I need to make this class plain, independent from any libraries (like annotations).
 * Because it's clean and easier to manage compared to one class with many annotations (from GSON, Room, etc.).
 * And when I have to use another library (like realm) this class will not need to change.
 *
 * But a trade-off is I have to create a lot of entity classes to handle each library.
 */
data class PlaceItem constructor(var name: String?,
                                 var latitude: Double,
                                 var longitude: Double,
                                 var type: Int,
                                 var isStarred: Boolean = false,
                                 var id: String = UUID.randomUUID().toString()) {

    fun isEmpty(): Boolean = name.isNullOrEmpty()
}