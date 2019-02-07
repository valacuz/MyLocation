package com.example.valacuz.mylocations.di.module

import android.content.Context
import com.example.valacuz.mylocations.data.repository.PlaceDataSource
import com.example.valacuz.mylocations.data.repository.PlaceTypeDataSource
import com.example.valacuz.mylocations.data.repository.memory.MemoryPlaceDataSource
import com.example.valacuz.mylocations.data.repository.mock.MockPlaceTypeDataSource
import com.example.valacuz.mylocations.data.repository.remote.RemotePlaceDataSource
import com.example.valacuz.mylocations.data.repository.remote.SamplePlaceService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module(includes = [RoomModule::class])
class DataSourceModule(private val context: Context) {

    @Provides
    fun providePlaceDataSource(): PlaceDataSource = MemoryPlaceDataSource.getInstance()

//    @Provides
//    fun providePlaceDataSource(retrofit: Retrofit): PlaceDataSource {
//        val sampleService: SamplePlaceService = retrofit.create(SamplePlaceService::class.java)
//        return RemotePlaceDataSource.getInstance(sampleService)
//    }

    @Provides
    fun providePlaceTypeSource(): PlaceTypeDataSource = MockPlaceTypeDataSource.getInstance()
}