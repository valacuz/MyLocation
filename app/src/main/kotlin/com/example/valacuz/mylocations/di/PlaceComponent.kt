package com.example.valacuz.mylocations.di

import com.example.valacuz.mylocations.di.module.NetModule
import com.example.valacuz.mylocations.di.module.PlaceDataModule
import com.example.valacuz.mylocations.di.module.PlaceMenuModule
import com.example.valacuz.mylocations.di.module.PlaceTypeDataModule
import com.example.valacuz.mylocations.form.PlaceFormActivity
import com.example.valacuz.mylocations.list.PlaceListActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    NetModule::class,
    PlaceMenuModule::class,
    PlaceDataModule::class,
    PlaceTypeDataModule::class]
)
interface PlaceComponent {
    fun inject(activity: PlaceListActivity)

    fun inject(activity: PlaceFormActivity)
}