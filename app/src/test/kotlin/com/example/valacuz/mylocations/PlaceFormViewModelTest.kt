package com.example.valacuz.mylocations

import com.example.valacuz.mylocations.data.PlaceType
import com.example.valacuz.mylocations.data.repository.PlaceDataSource
import com.example.valacuz.mylocations.data.repository.PlaceTypeDataSource
import com.example.valacuz.mylocations.form.PlaceFormMessageProvider
import com.example.valacuz.mylocations.form.PlaceFormNavigator
import com.example.valacuz.mylocations.form.PlaceFormViewModel
import com.example.valacuz.mylocations.util.schedulers.ImmediateSchdulerProvider
import io.reactivex.Flowable
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@RunWith(JUnit4::class)
class PlaceFormViewModelTest {

    private lateinit var viewModel: PlaceFormViewModel

    @Mock
    private lateinit var message: PlaceFormMessageProvider

    @Mock
    private lateinit var navigator: PlaceFormNavigator

    @Mock
    private lateinit var placeDataSource: PlaceDataSource

    @Mock
    private lateinit var typeDataSource: PlaceTypeDataSource

    private val schedulerProvider = ImmediateSchdulerProvider()

    private val typeItems = mutableListOf(
            PlaceType(1, "Education"),
            PlaceType(2, "Department store"),
            PlaceType(3, "Restaurant"),
            PlaceType(4, "Relaxation")
    )

    private val emptyNameMessage = "Name cannot be empty."

    @Before
    fun prepare() {
        MockitoAnnotations.initMocks(this)

        // Mock error message.
        Mockito.`when`(message.getErrorEmptyName()).thenReturn(emptyNameMessage)

        // Mock place types.
        Mockito.`when`(typeDataSource.getAllTypes()).thenReturn(Flowable.just(typeItems))

        // Create view model object.
        viewModel = PlaceFormViewModel(message, placeDataSource, typeDataSource, schedulerProvider)
        viewModel.setNavigator(navigator)
        viewModel.errorMessage.set(null)
    }

    @Test
    fun onCreated_populatePlaceTypes() {
        // Given PlaceFormViewModel and When its created.
        viewModel.create()

        // Then place type must be populated and have a same size as mock place type.
        assert(viewModel.placeTypes.size == typeItems.size)
    }

    @Test
    fun callLocationPicker_success() {
        // Mock action for navigator
        Mockito.`when`(navigator.goPickLocation(0.0, 0.0))
                .thenAnswer { /* Answer nothing, just check method was called or not. */ }

        // Given PlaceFormViewModel and When locationTextClick called.
        viewModel.locationTextClick()

        // Then navigator should bring us to pick location.
        Mockito.verify(navigator).goPickLocation(0.0, 0.0)
    }

    @Test
    fun addPlace_success() {
        // Mock action for navigator
        Mockito.`when`(navigator.goBackToList())
                .thenAnswer { /* Answer nothing, just check method was called or not */ }

        // Given information for place
        viewModel.name.set("Sample Place")
        viewModel.selectedType.set(typeItems[0])
        viewModel.latitude.set(13.00)
        viewModel.longitude.set(100.00)
        viewModel.starred.set(true)

        // When save the place
        viewModel.saveButtonClick()

        // Then the place should be saved & navigator bring us to place list
        Mockito.verify(navigator).goBackToList()
    }

    @Test
    fun addPlace_emptyPlaceName() {
        // Mock action for navigator
        Mockito.`when`(message.getErrorEmptyName()).thenReturn(emptyNameMessage)

        // Given information for place but leave place name empty.
        viewModel.name.set("")
        viewModel.selectedType.set(typeItems[0])
        viewModel.latitude.set(13.50)
        viewModel.longitude.set(100.50)
        viewModel.starred.set(false)

        // When save the place
        viewModel.saveButtonClick()

        // Then error message should be set (and display on view)
        assert(viewModel.errorMessage.get() == emptyNameMessage)
    }

    @Test
    fun updatePlace_success() {
        // Mock action for navigator
        Mockito.`when`(navigator.goBackToList())
                .thenAnswer { /* Answer nothing, just check method was called or not */ }

        val updateViewModel = PlaceFormViewModel(message, placeDataSource, typeDataSource,
                schedulerProvider, "RANDOM_ID")
        updateViewModel.setNavigator(navigator)

        // Given information for place
        updateViewModel.name.set("Updated Place")
        updateViewModel.selectedType.set(typeItems[1])
        updateViewModel.latitude.set(13.50)
        updateViewModel.longitude.set(100.50)
        updateViewModel.starred.set(false)

        // When save the place
        updateViewModel.saveButtonClick()

        // Then the place should be saved & navigator bring us to place list
        Mockito.verify(navigator).goBackToList()
    }

    @Test
    fun updatePlace_emptyPlaceName() {
        // Mock action for navigator
        Mockito.`when`(message.getErrorEmptyName()).thenReturn(emptyNameMessage)

        val updateViewModel = PlaceFormViewModel(message, placeDataSource, typeDataSource,
                schedulerProvider, "RANDOM_ID")
        updateViewModel.setNavigator(navigator)

        // Given information for place but leave place name empty.
        updateViewModel.name.set("")
        updateViewModel.selectedType.set(typeItems[1])
        updateViewModel.latitude.set(13.50)
        updateViewModel.longitude.set(100.50)
        updateViewModel.starred.set(false)

        // When save the place
        updateViewModel.saveButtonClick()

        // Then error message should be set (and display on view)
        assert(updateViewModel.errorMessage.get() == emptyNameMessage)
    }
}