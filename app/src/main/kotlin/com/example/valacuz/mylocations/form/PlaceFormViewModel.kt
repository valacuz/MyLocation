package com.example.valacuz.mylocations.form

import android.databinding.*
import com.example.valacuz.mylocations.data.PlaceDataSource
import com.example.valacuz.mylocations.data.PlaceItem
import com.example.valacuz.mylocations.data.PlaceType

class PlaceFormViewModel(itemDataSource: PlaceDataSource, id: String? = null) : BaseObservable() {

    // Binding fields.
    val name: ObservableField<String> = ObservableField()

    val latitude: ObservableDouble = ObservableDouble()

    val longitude: ObservableDouble = ObservableDouble()

    val coordinateString: ObservableField<String> = ObservableField()

    val placeTypes: ObservableList<PlaceType> = ObservableArrayList()

    val selectedType: ObservableField<PlaceType> = ObservableField()

    val starred: ObservableBoolean = ObservableBoolean(false)

    //

    private val mItemDataSource: PlaceDataSource = itemDataSource

    private var mNavigator: PlaceFormNavigator? = null

    private val mPlaceId: String? = id

    fun setNavigator(navigator: PlaceFormNavigator) {
        mNavigator = navigator
    }

    fun create() {
        // Retrieve list of place type.
        populatePlaceType()
        // Retrieve item from given id.
        mPlaceId?.let { populateItem(it) }
    }

    fun onActivityDestroyed() {
        mNavigator = null // Remove navigator references to avoid leaks.
    }

    fun saveButtonClick() {
        if (isNewLocation()) {
            addLocation(name.get(), selectedType.get().id,
                    latitude.get(), longitude.get(), starred.get())
        } else {
            updateLocation(name.get(), selectedType.get().id,
                    latitude.get(), longitude.get(), starred.get(), mPlaceId!!)
        }
    }

    fun locationTextClick() {
        mNavigator?.goPickLocation(latitude.get(), longitude.get())
    }

    fun setCoordinate(lat: Double, lon: Double) {
        latitude.set(lat)
        longitude.set(lon)
        coordinateString.set("%.6f, %.6f".format(latitude.get(), longitude.get()))
    }

    private fun isNewLocation(): Boolean = mPlaceId == null

    private fun populatePlaceType() {
        val allTypes = mItemDataSource.getAllTypes()
        allTypes?.let {
            placeTypes.addAll(it)
        }
    }

    private fun populateItem(placeId: String) {
        val placeItem = mItemDataSource.getById(placeId)
        if (placeItem != null) {
            name.set(placeItem.name)
            starred.set(placeItem.isStarred)
            setCoordinate(placeItem.latitude, placeItem.longitude)
            selectedType.set(placeTypes.first { it.id == placeItem.type })
        }
    }

    private fun addLocation(name: String,
                            type: Int,
                            latitude: Double,
                            longitude: Double,
                            isStarred: Boolean) {
        val place = PlaceItem(name, latitude, longitude, type, isStarred)
        mItemDataSource.addPlace(place)
        mNavigator?.goBackToList()
    }

    private fun updateLocation(name: String,
                               type: Int,
                               latitude: Double,
                               longitude: Double,
                               isStarred: Boolean,
                               id: String) {
        val place = PlaceItem(name, latitude, longitude, type, isStarred, id)
        mItemDataSource.updatePlace(place)
        mNavigator?.goBackToList()
    }
}