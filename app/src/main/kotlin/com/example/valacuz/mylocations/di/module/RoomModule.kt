package com.example.valacuz.mylocations.di.module

import android.content.Context
import com.example.valacuz.mylocations.data.repository.room.AppDatabase
import com.example.valacuz.mylocations.data.repository.room.PlaceDao
import com.example.valacuz.mylocations.data.repository.room.PlaceTypeDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RoomModule(val context: Context) {

    @Singleton
    @Provides
    fun provideRoomDatabase(): AppDatabase = AppDatabase.getInstance(context)

    @Singleton
    @Provides
    fun providePlaceDao(appDatabase: AppDatabase): PlaceDao = appDatabase.placeItemDao()

    @Singleton
    @Provides
    fun providePlaceTypeDao(appDatabase: AppDatabase): PlaceTypeDao = appDatabase.placeTypeDao()
}