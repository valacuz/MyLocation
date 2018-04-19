package com.example.valacuz.mylocations.di.module

import android.content.Context
import com.example.valacuz.mylocations.data.repository.PlaceDataSource
import com.example.valacuz.mylocations.data.repository.PlaceTypeDataSource
import com.example.valacuz.mylocations.data.repository.mock.MockPlaceDataSource
import com.example.valacuz.mylocations.data.repository.mock.MockPlaceTypeDataSource
import dagger.Module
import dagger.Provides

@Module(includes = [RoomModule::class])
class DataSourceModule(private val context: Context) {

    @Provides
    fun providePlaceDataSource(): PlaceDataSource = MockPlaceDataSource.getInstance()

    @Provides
    fun providePlaceTypeSource(): PlaceTypeDataSource = MockPlaceTypeDataSource.getInstance()
}