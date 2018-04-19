package com.example.valacuz.mylocations

import com.example.valacuz.mylocations.model.*
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
        MemoryPlaceDataSourceTest::class,
        MemoryPlaceTypeDataSourceTest::class,
        RoomPlaceDataSourceTest::class,
        RoomPlaceTypeDataSourceTest::class,
        RealmPlaceDataSourceTest::class,
        RealmPlaceTypeDataSourceTest::class
)
class ModelTestSuite