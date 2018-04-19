package com.example.valacuz.mylocations.model

import com.example.valacuz.mylocations.data.PlaceType
import com.example.valacuz.mylocations.data.repository.PlaceTypeDataSource
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.concurrent.TimeUnit

/**
 * Base class for testing [PlaceTypeDataSource]
 */
abstract class PlaceTypeDataSourceTest {

    private lateinit var dataSource: PlaceTypeDataSource

    abstract fun getPlaceTypeDataSource(): PlaceTypeDataSource

    abstract fun doPrepare()

    abstract fun doCleanUp()

    @Before
    fun prepare() {
        doPrepare()
        dataSource = getPlaceTypeDataSource()
        dataSource.clearTypes()
    }

    @After
    fun cleanUp() {
        doCleanUp()
    }

    @Test
    fun addPlaces_retrieveAllPlaces() {
        // Given list of place type which contains 3 items
        val types = mutableListOf(FIRST_TYPE, SECOND_TYPE, THIRD_TYPE)

        // When add them into repository
        dataSource.addTypes(types)

        // Then all 3 types can be retrieved from repository
        dataSource.getAllTypes()
                .test()
                .awaitDone(3, TimeUnit.SECONDS) // Wait a few second database in working.
                .assertValue { items ->
                    items.isNotEmpty() &&
                            items.size == types.size &&
                            items[0] == FIRST_TYPE &&
                            items[1] == SECOND_TYPE &&
                            items[2] == THIRD_TYPE
                }
    }

    @Test
    fun clearPlace_retrievePlacesIsNull() {
        // Given list of place type which contains 3 items
        val types = mutableListOf(FIRST_TYPE, SECOND_TYPE, THIRD_TYPE)

        // When add them into repository
        dataSource.addTypes(types)

        // And clear all place types
        dataSource.clearTypes()

        // Then repository should return empty list of place types
        dataSource.getAllTypes()
                .test()
                .awaitDone(3, TimeUnit.SECONDS) // Wait a few second database in working.
                .assertValue { it.isEmpty() }
    }

    companion object {
        private val FIRST_TYPE = PlaceType(1, "Education")
        private val SECOND_TYPE = PlaceType(2, "Department store")
        private val THIRD_TYPE = PlaceType(3, "Restaurant")
    }
}