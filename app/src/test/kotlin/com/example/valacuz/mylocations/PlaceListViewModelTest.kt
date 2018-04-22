package com.example.valacuz.mylocations

import com.example.valacuz.mylocations.data.PlaceItem
import com.example.valacuz.mylocations.data.repository.PlaceDataSource
import com.example.valacuz.mylocations.list.PlaceListViewModel
import com.example.valacuz.mylocations.list.PlaceNavigator
import com.example.valacuz.mylocations.util.schedulers.ImmediateSchdulerProvider
import io.reactivex.Flowable
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@RunWith(JUnit4::class)
class PlaceListViewModelTest {

    private lateinit var viewModel: PlaceListViewModel

    @Mock
    private lateinit var navigator: PlaceNavigator

    @Mock
    private lateinit var dataSource: PlaceDataSource

    private val schedulerProvider = ImmediateSchdulerProvider()

    private val placeItems = mutableListOf(
            PlaceItem("Chulalongkorn university", 13.7419273, 100.5256927, 1, true),
            PlaceItem("The old siam", 13.7492849, 100.4989994, 2, false),
            PlaceItem("Bobae Tower", 13.7492849, 100.4989994, 2, false),
            PlaceItem("Grand china hotel", 13.7423837, 100.5075352, 4, true)
    )

    @Before
    fun prepare() {
        // Start processing annotations.
        MockitoAnnotations.initMocks(this)

        Mockito.`when`(navigator.addLocation())
                .thenAnswer { /* Answer nothing, just check method was called or not. */ }

        Mockito.`when`(dataSource.getAllPlaces())
                .thenReturn(Flowable.just(placeItems))

        // Create view model object.
        viewModel = PlaceListViewModel(dataSource, schedulerProvider)
        viewModel.setNavigator(navigator)
    }

    @Test
    fun loadPlaces_shouldHaveItems() {
        // Given 4 places for PlaceDataSource
        Mockito.`when`(dataSource.getAllPlaces())
                .thenReturn(Flowable.just(placeItems))

        // When view model call loadPlaces()
        viewModel.loadItems()

        // Then places must be retrieved and contains 4 item
        assertEquals(4, viewModel.items.size)
    }

    @Test
    fun deletePlaces_shouldSuccess() {
        // Given 4 places for PlaceDataSource
        Mockito.`when`(dataSource.getAllPlaces())
                .thenReturn(Flowable.just(placeItems))

        // When view model call loadPlaces()
        viewModel.loadItems()

        // And delete 2 items
        viewModel.onDeletePlaceClick(placeItems[1])
        viewModel.onDeletePlaceClick(placeItems[0])

        // Then places must be retrieved and contains 4 item
        assertEquals(2, viewModel.items.size)
    }

    @Test
    fun requestAddPlace_shouldNavigate() {
        // When view model call add new task.
        viewModel.addNewTask()

        // Then navigator should bring us to add location.
        Mockito.verify(navigator).addLocation()
    }
}