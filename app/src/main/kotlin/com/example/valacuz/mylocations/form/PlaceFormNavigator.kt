package com.example.valacuz.mylocations.form

interface PlaceFormNavigator {

    fun goBackToList()

    fun goPickLocation(latitude: Double?, longitude: Double?)
}