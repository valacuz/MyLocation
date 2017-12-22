package com.example.valacuz.mylocations

import com.example.valacuz.mylocations.data.PlaceDataSource
import com.example.valacuz.mylocations.data.PlaceItem
import com.example.valacuz.mylocations.data.repository.MemoryPlaceDataSource
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@Suppress("FunctionName")
class PlaceDataSourceTest {

    private lateinit var mDataSource: PlaceDataSource

    @Before
    fun setup() {
        mDataSource = MemoryPlaceDataSource.INSTANCE
    }

    @After
    fun cleanup() {
        mDataSource.clearPlace()
    }

    @Test
    fun testPrecondition() {
        assertNotNull(mDataSource)
    }

    @Test
    fun addPlace_retrievePlace() {
        // Given a new place
        val place = PlaceItem("House", 13.7372904, 100.5380263, 1, true)

        // When save it into repository
        mDataSource.addPlace(place)

        // Then the place can be retrieved from repository
        val savedPlace = mDataSource.getById(place.id)

        // And the place information retrieved from repository must equals to given information
        if (savedPlace != null) {
            assertEquals("House", savedPlace.name)
            assertEquals(13.7372904, savedPlace.latitude, 0.001)
            assertEquals(100.5380263, savedPlace.longitude, 0.001)
            assertTrue(savedPlace.isStarred)
        } else {
            fail("Cannot retrieve place from given id")
        }
    }

    @Test
    fun addPlaces_retrieveAllPlace() {
        // Given 2 new places in repository
        val firstPlace = PlaceItem("First House", 13.7372904, 100.5380263, 1, true)
        val secondPlace = PlaceItem("Second House", 10.7372904, 100.5380263, 1, false)

        // When save 2 place into repository
        mDataSource.addPlace(firstPlace)
        mDataSource.addPlace(secondPlace)

        // Then both 2 places can be retrieved from repository
        val places = mDataSource.getAllPlaces()
        if (places != null && places.isNotEmpty()) {
            // Assert first place
            places[0].let {
                assertEquals("First House", it.name)
                assertEquals(13.7372904, it.latitude, 0.001)
                assertEquals(100.5380263, it.longitude, 0.001)
                assertEquals(1, it.type)
                assertTrue(it.isStarred)
            }
            // Assert second place
            places[1].let {
                assertEquals("Second House", it.name)
                assertEquals(10.7372904, it.latitude, 0.001)
                assertEquals(100.5380263, it.longitude, 0.001)
                assertEquals(1, it.type)
                assertFalse(it.isStarred)
            }
        } else {
            fail("Cannot retrieve place from repository")
        }
    }

    @Test
    fun updatePlace_retrievePlace() {
        // Given a new place
        val place = PlaceItem("House", 13.7372904, 100.5380263, 1, false)

        // When save it into repository
        mDataSource.addPlace(place)

        // And edit place information and update to repository
        val editedPlace = PlaceItem("Hospital", 13.7308651, 100.5268335, 2, true, place.id)
        mDataSource.updatePlace(editedPlace)

        // Then the place can be retrieved from repository
        val savedPlace = mDataSource.getById(place.id)

        // And the place information retrieved from repository must be equals to edited information
        if (savedPlace != null) {
            assertEquals("Hospital", savedPlace.name)
            assertEquals(13.7308651, savedPlace.latitude, 0.001)
            assertEquals(100.5268335, savedPlace.longitude, 0.001)
            assertTrue(savedPlace.isStarred)
        } else {
            fail("Cannot retrieve place from given id")
        }
    }

    @Test
    fun deletePlace_retrievePlace() {
        // Given a new place in repository
        val place = PlaceItem("House", 13.7372904, 100.5380263, 1, false)
        mDataSource.addPlace(place)

        // When remove place from repository
        mDataSource.deletePlace(place)

        // Then the place must not be retrieve from repository
        val savedPlace = mDataSource.getById(place.id)
        assertNull(savedPlace)
    }

    @Test
    fun clearPlace_retrievePlacesIsNull() {
        // Given 2 new place in repository
        val firstPlace = PlaceItem("First House", 13.7372904, 100.5380263, 1, false)
        mDataSource.addPlace(firstPlace)

        val secondPlace = PlaceItem("Second House", 10.7372904, 100.5380263, 1, false)
        mDataSource.addPlace(secondPlace)

        // When remove all place from repository
        mDataSource.clearPlace()

        // Then the place must not be retrieve from repository
        val allPlaces = mDataSource.getAllPlaces()
        assertTrue(allPlaces == null || allPlaces.isEmpty())
    }
}