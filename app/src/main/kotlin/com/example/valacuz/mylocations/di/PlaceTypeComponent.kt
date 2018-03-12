package com.example.valacuz.mylocations.di

import com.example.valacuz.mylocations.form.PlaceFormActivity
import com.example.valacuz.mylocations.list.PlaceListActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(NetModule::class), (PlaceTypeModule::class)])
interface PlaceTypeComponent {
    fun inject(activity: PlaceListActivity)

    fun inject(activity: PlaceFormActivity)
}