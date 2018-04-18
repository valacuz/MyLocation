package com.example.valacuz.mylocations.model

import com.example.valacuz.mylocations.data.PlaceItem
import com.example.valacuz.mylocations.data.repository.PlaceDataSource
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.concurrent.TimeUnit

/**
 * Base class for testing [PlaceDataSource]
 */
abstract class PlaceDataSourceTest {

    private lateinit var dataSource: PlaceDataSource

    abstract fun getPlaceDataSource(): PlaceDataSource

    abstract fun doPrepare()

    abstract fun doCleanUp()

    @Before
    fun prepare() {
        doPrepare()
        dataSource = getPlaceDataSource()
        dataSource.clearPlaces()
    }

    @After
    fun cleanUp() {
        doCleanUp()
    }

    @Test
    fun addPlace_retrievePlace() {
        // Given a new place & When save it into repository
        dataSource.addPlace(FIRST_PLACE)

        // Then the place can be retrieved from repository
        dataSource.getById(FIRST_PLACE.id)
                .test()
                .awaitDone(3, TimeUnit.SECONDS) // Wait a few second database in working.
                .assertValue { it == FIRST_PLACE }
    }

    @Test
    fun addPlaces_retrieveAllPlaces() {
        // Given 2 new places & When add these places into repository
        dataSource.addPlace(FIRST_PLACE)
        dataSource.addPlace(SECOND_PLACE)

        // Then both 2 places can be retrieved from repository
        dataSource.getAllPlaces()
                .test()
                .awaitDone(3, TimeUnit.SECONDS) // Wait a few second database in working.
                .assertValue { items ->
                    items.isNotEmpty() &&
                            items[0] == FIRST_PLACE &&
                            items[1] == SECOND_PLACE
                }
    }

    @Test
    fun updatePlace_retrievePlace() {
        val editedPlace = PlaceItem(THIRD_PLACE.name,
                THIRD_PLACE.latitude,
                THIRD_PLACE.longitude,
                THIRD_PLACE.type,
                THIRD_PLACE.isStarred,
                FIRST_PLACE.id)

        // Given a new place & Save it into repository
        dataSource.addPlace(FIRST_PLACE)

        // When edit place information and update to repository
        dataSource.updatePlace(editedPlace)

        // Then the place can be retrieved from repository with updated information
        dataSource.getById(FIRST_PLACE.id)
                .test()
                .awaitDone(3, TimeUnit.SECONDS) // Wait a few second database in working.
                .assertValue { it == editedPlace }
    }

    @Test
    fun deletePlace_retrievePlace() {
        // Given a new place in repository
        dataSource.addPlace(FIRST_PLACE)

        // When remove place from repository
        dataSource.deletePlace(FIRST_PLACE)

        // Then the place must not be retrieve from repository
        dataSource.getById(FIRST_PLACE.id)
                .test()
                .awaitDone(3, TimeUnit.SECONDS) // Wait a few second database in working.
                .assertNoValues()
    }

    @Test
    fun clearPlace_retrievePlacesIsNull() {
        // Given 2 new places in repository
        dataSource.addPlace(FIRST_PLACE)
        dataSource.addPlace(SECOND_PLACE)

        // When remove all places from repository
        dataSource.clearPlaces()

        // Then repository should return empty list of place
        dataSource.getAllPlaces()
                .test()
                .awaitDone(3, TimeUnit.SECONDS) // Wait a few second database in working.
                .assertValue { it.isEmpty() }
    }

    companion object {
        private val FIRST_PLACE = PlaceItem("Chulalongkorn",
                13.741734,
                100.516680,
                1,
                false)
        private val SECOND_PLACE = PlaceItem("MBK",
                13.743490,
                100.530778,
                2,
                false)
        private val THIRD_PLACE = PlaceItem("Dice Cup Board Game Cafe",
                13.743240,
                100.527709,
                4,
                true)
    }
}