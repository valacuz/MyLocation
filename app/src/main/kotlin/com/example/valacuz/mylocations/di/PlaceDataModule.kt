package com.example.valacuz.mylocations.di

import android.content.Context
import com.example.valacuz.mylocations.data.repository.CompositePlaceDataSource
import com.example.valacuz.mylocations.data.repository.PlaceDataSource
import com.example.valacuz.mylocations.data.repository.memory.MemoryPlaceDataSource
import com.example.valacuz.mylocations.data.repository.remote.RemotePlaceDataSource
import com.example.valacuz.mylocations.data.repository.remote.SamplePlaceService
import com.example.valacuz.mylocations.data.repository.room.RoomPlaceDataSource
import dagger.Module
import dagger.Provides

@Module
class PlaceDataModule(private val context: Context) {

    @Provides
    fun providePlace(samplePlaceService: SamplePlaceService): PlaceDataSource =
            CompositePlaceDataSource.getInstance(
                    MemoryPlaceDataSource.getInstance(),
                    RoomPlaceDataSource.getInstance(context.applicationContext),
                    RemotePlaceDataSource.getInstance(samplePlaceService))
}