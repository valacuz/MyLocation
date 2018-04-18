package com.example.valacuz.mylocations

import com.example.valacuz.mylocations.model.RealmPlaceDataSourceTest
import com.example.valacuz.mylocations.model.RealmPlaceTypeDataSourceTest
import com.example.valacuz.mylocations.model.RoomPlaceDataSourceTest
import com.example.valacuz.mylocations.model.RoomPlaceTypeDataSourceTest
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
        RoomPlaceDataSourceTest::class,
        RoomPlaceTypeDataSourceTest::class,
        RealmPlaceDataSourceTest::class,
        RealmPlaceTypeDataSourceTest::class
)
class ModelTestSuite