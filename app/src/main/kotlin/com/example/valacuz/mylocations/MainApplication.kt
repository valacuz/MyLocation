package com.example.valacuz.mylocations

import android.app.Application
import com.example.valacuz.mylocations.di.DaggerPlaceComponent
import com.example.valacuz.mylocations.di.PlaceComponent
import com.example.valacuz.mylocations.di.module.NetModule
import com.example.valacuz.mylocations.di.module.DataSourceModule
import com.example.valacuz.mylocations.di.module.PlaceMenuModule
import com.example.valacuz.mylocations.di.module.RoomModule
import io.realm.Realm
import io.realm.RealmConfiguration

open class MainApplication : Application() {

    lateinit var placeComponent: PlaceComponent

    override fun onCreate() {
        super.onCreate()

        configureRealm()    // Config realm database
        placeComponent = createPlaceTypeComponent() // Config dagger2
    }

    protected open fun createPlaceTypeComponent(): PlaceComponent {
        return DaggerPlaceComponent.builder()
                .netModule(NetModule("https://private-96860-valacuz.apiary-mock.com/"))
                .roomModule(RoomModule(this.applicationContext))
                .placeMenuModule(PlaceMenuModule(this.applicationContext))
                .dataSourceModule(DataSourceModule(this.applicationContext))
                .build()
    }

    private fun configureRealm() {
        Realm.init(this)

        val config = RealmConfiguration.Builder()
                .name("realm_my_location.realm")
                .build()
        Realm.setDefaultConfiguration(config)
    }
}