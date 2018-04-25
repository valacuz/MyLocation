package com.example.valacuz.mylocations

import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
        PlaceListViewModelTest::class,
        PlaceFormViewModelTest::class
)
class ViewModelTestSuite