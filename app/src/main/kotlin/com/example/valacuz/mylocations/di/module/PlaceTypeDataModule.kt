package com.example.valacuz.mylocations.di.module

import android.content.Context
import com.example.valacuz.mylocations.data.repository.CompositePlaceTypeDataSource
import com.example.valacuz.mylocations.data.repository.PlaceTypeDataSource
import com.example.valacuz.mylocations.data.repository.memory.MemoryPlaceTypeDataSource
import com.example.valacuz.mylocations.data.repository.remote.RemotePlaceTypeDataSource
import com.example.valacuz.mylocations.data.repository.remote.SamplePlaceService
import com.example.valacuz.mylocations.data.repository.room.RoomPlaceTypeDataSource
import dagger.Module
import dagger.Provides

@Module
class PlaceTypeDataModule(private val context: Context) {

    @Provides
    fun providePlaceType(samplePlaceService: SamplePlaceService): PlaceTypeDataSource =
            CompositePlaceTypeDataSource.getInstance(
                    MemoryPlaceTypeDataSource.getInstance(),
                    RoomPlaceTypeDataSource.getInstance(context.applicationContext),
                    RemotePlaceTypeDataSource.getInstance(samplePlaceService))
}