package com.example.valacuz.mylocations.di

import android.app.Application

open class MainApplication : Application() {

    lateinit var placeComponent: PlaceComponent

    override fun onCreate() {
        super.onCreate()

        placeComponent = createPlaceTypeComponent()
    }

    protected open fun createPlaceTypeComponent(): PlaceComponent {
        return DaggerPlaceComponent
                .builder()
                .placeDataModule(PlaceDataModule(this.applicationContext))
                .placeTypeDataModule(PlaceTypeDataModule(this.applicationContext))
                .netModule(NetModule("https://private-96860-valacuz.apiary-mock.com/"))
                .build()
    }
}