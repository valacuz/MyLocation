package com.example.valacuz.mylocations.di

import android.app.Application
import com.example.valacuz.mylocations.di.module.NetModule
import com.example.valacuz.mylocations.di.module.PlaceDataModule
import com.example.valacuz.mylocations.di.module.PlaceMenuModule
import com.example.valacuz.mylocations.di.module.PlaceTypeDataModule

open class MainApplication : Application() {

    lateinit var placeComponent: PlaceComponent

    override fun onCreate() {
        super.onCreate()

        placeComponent = createPlaceTypeComponent()
    }

    protected open fun createPlaceTypeComponent(): PlaceComponent {
        return DaggerPlaceComponent
                .builder()
                .netModule(NetModule("https://private-96860-valacuz.apiary-mock.com/"))
                .placeMenuModule(PlaceMenuModule(this.applicationContext))
                .placeDataModule(PlaceDataModule(this.applicationContext))
                .placeTypeDataModule(PlaceTypeDataModule(this.applicationContext))
                .build()
    }
}