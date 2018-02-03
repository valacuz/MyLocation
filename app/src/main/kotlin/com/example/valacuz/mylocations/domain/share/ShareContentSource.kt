package com.example.valacuz.mylocations.domain.share

interface ShareContentSource {

    fun shareContent(name: String, latitude: Double, longitude: Double)
}