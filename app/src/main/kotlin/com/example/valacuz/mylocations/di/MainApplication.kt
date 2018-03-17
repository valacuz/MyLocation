package com.example.valacuz.mylocations.di

import android.app.Application
import com.example.valacuz.mylocations.di.module.NetModule
import com.example.valacuz.mylocations.di.module.DataSourceModule
import com.example.valacuz.mylocations.di.module.PlaceMenuModule
import com.example.valacuz.mylocations.di.module.RoomModule

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
                .roomModule(RoomModule(this.applicationContext))
                .placeMenuModule(PlaceMenuModule(this.applicationContext))
                .dataSourceModule(DataSourceModule(this.applicationContext))
                .build()
    }
}