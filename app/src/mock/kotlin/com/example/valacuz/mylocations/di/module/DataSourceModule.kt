package com.example.valacuz.mylocations.di.module

import android.content.Context
import com.example.valacuz.mylocations.data.repository.CompositePlaceDataSource
import com.example.valacuz.mylocations.data.repository.CompositePlaceTypeDataSource
import com.example.valacuz.mylocations.data.repository.PlaceDataSource
import com.example.valacuz.mylocations.data.repository.PlaceTypeDataSource
import com.example.valacuz.mylocations.data.repository.memory.MemoryPlaceDataSource
import com.example.valacuz.mylocations.data.repository.memory.MemoryPlaceTypeDataSource
import com.example.valacuz.mylocations.data.repository.remote.RemotePlaceDataSource
import com.example.valacuz.mylocations.data.repository.remote.RemotePlaceTypeDataSource
import com.example.valacuz.mylocations.data.repository.remote.SamplePlaceService
import com.example.valacuz.mylocations.data.repository.room.PlaceDao
import com.example.valacuz.mylocations.data.repository.room.PlaceTypeDao
import com.example.valacuz.mylocations.data.repository.room.RoomPlaceDataSource
import com.example.valacuz.mylocations.data.repository.room.RoomPlaceTypeDataSource
import dagger.Module
import dagger.Provides

@Module(includes = [RoomModule::class])
class DataSourceModule(private val context: Context) {

    @Provides
    fun providePlaceDataSource(placeDao: PlaceDao,
                               samplePlaceService: SamplePlaceService): PlaceDataSource =
            CompositePlaceDataSource.getInstance(
                    MemoryPlaceDataSource.getInstance(),
                    RoomPlaceDataSource.getInstance(placeDao, context.applicationContext),
                    RemotePlaceDataSource.getInstance(samplePlaceService))

    @Provides
    fun providePlaceTypeSource(placeTypeDao: PlaceTypeDao,
                               samplePlaceService: SamplePlaceService): PlaceTypeDataSource =
            CompositePlaceTypeDataSource.getInstance(
                    MemoryPlaceTypeDataSource.getInstance(),
                    RoomPlaceTypeDataSource.getInstance(placeTypeDao, context.applicationContext),
                    RemotePlaceTypeDataSource.getInstance(samplePlaceService))

}