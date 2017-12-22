package com.example.valacuz.mylocations.list

import com.example.valacuz.mylocations.data.PlaceItem

interface PlaceItemNavigator {

    fun displayDetail(id: String)

    fun displayItemAction(place: PlaceItem)
}