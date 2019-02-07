package com.example.valacuz.mylocations.form

interface PlaceFormNavigator {

    fun dispatchToPlaceList()

    fun dispatchPickLocation(latitude: Double?, longitude: Double?)

    fun dispatchTakePicture()
}