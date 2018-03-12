package com.example.valacuz.mylocations.di

import android.app.Application

open class MainApplication : Application() {

    lateinit var placeTypeComponent: PlaceTypeComponent

    override fun onCreate() {
        super.onCreate()

        placeTypeComponent = createPlaceTypeComponent()
    }

    protected open fun createPlaceTypeComponent(): PlaceTypeComponent {
        return DaggerPlaceTypeComponent
                .builder()
                .placeTypeModule(PlaceTypeModule(this.applicationContext))
                .netModule(NetModule("https://private-96860-valacuz.apiary-mock.com/"))
                .build()
    }
}