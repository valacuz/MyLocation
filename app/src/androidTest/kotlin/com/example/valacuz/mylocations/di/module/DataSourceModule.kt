package com.example.valacuz.mylocations.di.module

import com.example.valacuz.mylocations.data.repository.PlaceDataSource
import com.example.valacuz.mylocations.data.repository.PlaceTypeDataSource
import com.example.valacuz.mylocations.data.repository.mock.MockPlaceDataSource
import com.example.valacuz.mylocations.data.repository.mock.MockPlaceTypeDataSource
import dagger.Module
import dagger.Provides

@Module
class DataSourceModule {

    @Provides
    fun providePlaceDataSource(): PlaceDataSource = MockPlaceDataSource.getInstance()

    @Provides
    fun providePlaceTypeDataSource(): PlaceTypeDataSource = MockPlaceTypeDataSource.getInstance()
}