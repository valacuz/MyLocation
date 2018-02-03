package com.example.valacuz.mylocations

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.example.valacuz.mylocations.data.PlaceItem
import com.example.valacuz.mylocations.data.repository.room.AppDatabase
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@Suppress("FunctionName")
class PlaceDataSourceTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var appDatabase: AppDatabase

    @Before
    fun setup() {
        appDatabase = Room
                .inMemoryDatabaseBuilder(InstrumentationRegistry.getTargetContext(), AppDatabase::class.java)
                .allowMainThreadQueries()   // Allow main thread queries, just for testing
                .build()
    }

    @After
    fun cleanup() {
        appDatabase.placeItemDao().clearPlaces()
        appDatabase.close()
    }

    @Test
    fun addPlace_retrievePlace() {
        // Given a new place & When save it into repository
        appDatabase.placeItemDao().addPlace(FIRST_PLACE)

        // Then the place can be retrieved from repository
        appDatabase.placeItemDao()
                .getById(FIRST_PLACE.id)
                .test()
                .assertValue({
                    it.name.equals(FIRST_PLACE.name) &&
                            it.latitude == FIRST_PLACE.latitude &&
                            it.longitude == FIRST_PLACE.longitude &&
                            it.isStarred == FIRST_PLACE.isStarred
                })
    }

    @Test
    fun addPlaces_retrieveAllPlace() {
        // Given 2 new places & When these places into repository
        appDatabase.placeItemDao().addPlace(FIRST_PLACE)
        appDatabase.placeItemDao().addPlace(SECOND_PLACE)

        // Then both 2 places can be retrieved from repository
        appDatabase.placeItemDao()
                .getAllPlaces()
                .test()
                .assertValue({
                    it.isNotEmpty() &&
                            it[0].let {
                                it.name.equals(FIRST_PLACE.name) &&
                                        it.latitude == FIRST_PLACE.latitude &&
                                        it.longitude == FIRST_PLACE.longitude &&
                                        it.isStarred == FIRST_PLACE.isStarred
                            } &&
                            it[1].let {
                                it.name.equals(SECOND_PLACE.name) &&
                                        it.latitude == SECOND_PLACE.latitude &&
                                        it.longitude == SECOND_PLACE.longitude &&
                                        it.isStarred == SECOND_PLACE.isStarred
                            }
                })
    }

    @Test
    fun updatePlace_retrievePlace() {
        // Given a new place & When save it into repository
        appDatabase.placeItemDao().addPlace(FIRST_PLACE)

        // And edit place information and update to repository
        val editedPlace = PlaceItem(THIRD_PLACE.name,
                THIRD_PLACE.latitude,
                THIRD_PLACE.longitude,
                THIRD_PLACE.type,
                THIRD_PLACE.isStarred,
                FIRST_PLACE.id)
        appDatabase.placeItemDao().updatePlace(editedPlace)

        // Then the place can be retrieved from repository
        appDatabase.placeItemDao()
                .getById(FIRST_PLACE.id)
                .test()
                .assertValue({
                    it.name.equals(THIRD_PLACE.name) &&
                            it.latitude == THIRD_PLACE.latitude &&
                            it.longitude == THIRD_PLACE.longitude &&
                            it.type == THIRD_PLACE.type &&
                            it.isStarred == THIRD_PLACE.isStarred &&
                            it.id == FIRST_PLACE.id
                })
    }

    @Test
    fun deletePlace_retrievePlace() {
        // Given a new place in repository
        appDatabase.placeItemDao().addPlace(FIRST_PLACE)

        // When remove place from repository
        appDatabase.placeItemDao().deletePlace(FIRST_PLACE)

        // Then the place must not be retrieve from repository
        appDatabase.placeItemDao()
                .getById(FIRST_PLACE.id)
                .test()
                .assertNoValues()
    }

    @Test
    fun clearPlace_retrievePlacesIsNull() {
        // Given 2 new place in repository
        appDatabase.placeItemDao().addPlace(FIRST_PLACE)
        appDatabase.placeItemDao().addPlace(SECOND_PLACE)

        // When remove all place from repository
        appDatabase.placeItemDao().clearPlaces()

        // Then the place must not be retrieve from repository
        appDatabase.placeItemDao()
                .getAllPlaces()
                .test()
                .assertValue({
                    it.isEmpty()
                })
    }

    private const val FIRST_PLACE = PlaceItem("CHULA",
            13.741734,
            100.516680,
            1,
            false)
    private const val SECOND_PLACE = PlaceItem("MBK",
            13.743490,
            100.530778,
            2,
            false)
    private const val THIRD_PLACE = PlaceItem("Dice Cup Board Game Cafe",
            13.743240,
            100.527709,
            4,
            true)
}