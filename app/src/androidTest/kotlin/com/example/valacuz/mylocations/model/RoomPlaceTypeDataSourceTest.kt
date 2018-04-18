package com.example.valacuz.mylocations.model

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.example.valacuz.mylocations.data.repository.PlaceTypeDataSource
import com.example.valacuz.mylocations.data.repository.room.AppDatabase
import com.example.valacuz.mylocations.data.repository.room.RoomPlaceTypeDataSource
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RoomPlaceTypeDataSourceTest : PlaceTypeDataSourceTest() {

    private lateinit var appDatabase: AppDatabase

    override fun doPrepare() {
        appDatabase = Room
                .inMemoryDatabaseBuilder(InstrumentationRegistry.getTargetContext(), AppDatabase::class.java)
                .allowMainThreadQueries()   // Allow main thread queries, just for testing
                .build()
    }

    override fun doCleanUp() {
        // Nothing to cleanup in memory database.
    }

    override fun getPlaceTypeDataSource(): PlaceTypeDataSource {
        val typeDao = appDatabase.placeTypeDao()
        return RoomPlaceTypeDataSource.getInstance(typeDao, InstrumentationRegistry.getTargetContext())
    }
}