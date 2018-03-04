package com.example.valacuz.mylocations.form

import android.content.Context
import android.databinding.*
import com.example.valacuz.mylocations.R
import com.example.valacuz.mylocations.data.repository.PlaceDataSource
import com.example.valacuz.mylocations.data.PlaceItem
import com.example.valacuz.mylocations.data.PlaceType
import com.example.valacuz.mylocations.data.repository.PlaceTypeDataSource
import com.example.valacuz.mylocations.util.ScheduleStrategy
import io.reactivex.disposables.CompositeDisposable

class PlaceFormViewModel(context: Context,
                         private val itemDataSource: PlaceDataSource,
                         private val typeDataSource: PlaceTypeDataSource,
                         private val scheduleStrategy: ScheduleStrategy,
                         id: String? = null)
    : BaseObservable() {

    // Binding fields.
    val name: ObservableField<String> = ObservableField()

    val latitude: ObservableDouble = ObservableDouble(0.0)

    val longitude: ObservableDouble = ObservableDouble(0.0)

    val selectedType: ObservableField<PlaceType> = ObservableField()

    val starred: ObservableBoolean = ObservableBoolean(false)

    // Used to display error messages
    val errorMessage: ObservableField<String> = ObservableField()

    // Used to display coordinate in format {lat}, {lon}
    val coordinateString: ObservableField<String> = ObservableField()

    // Used to binding with spinner for types of place
    val placeTypes: ObservableList<PlaceType> = ObservableArrayList()

    //
    private val context: Context = context.applicationContext  // Force application context to avoid leaks

    private var navigator: PlaceFormNavigator? = null

    private val placeId: String? = id

    //
    private val compositeDisposable = CompositeDisposable()

    fun setNavigator(navigator: PlaceFormNavigator) {
        this.navigator = navigator
    }

    fun create() {
        // Retrieve list of place type.
        populatePlaceType()
        // Retrieve item from given id.
        placeId?.let { populateItem(it) }
    }

    fun onActivityDestroyed() {
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
        navigator = null // Remove navigator references to avoid leaks.
    }

    fun saveButtonClick() {
        if (isNewLocation()) {
            addLocation(name.get(), selectedType.get().id,
                    latitude.get(), longitude.get(), starred.get())
        } else {
            updateLocation(name.get(), selectedType.get().id,
                    latitude.get(), longitude.get(), starred.get(), placeId!!)
        }
    }

    fun locationTextClick() {
        navigator?.goPickLocation(latitude.get(), longitude.get())
    }

    fun setCoordinate(lat: Double, lon: Double) {
        latitude.set(lat)
        longitude.set(lon)
        coordinateString.set("%.6f, %.6f".format(latitude.get(), longitude.get()))
    }

    private fun isNewLocation(): Boolean = placeId == null

    private fun populatePlaceType() {
        val disposable = typeDataSource.getAllTypes()
                .compose(scheduleStrategy.applySchedule())
                .subscribe({ types -> placeTypes.addAll(types) })
        compositeDisposable.add(disposable)
    }

    private fun populateItem(placeId: String) {
        val disposable = itemDataSource.getById(placeId)
                .compose(scheduleStrategy.applySchedule())
                .subscribe({ placeItem ->
                    name.set(placeItem.name)
                    starred.set(placeItem.isStarred)
                    setCoordinate(placeItem.latitude, placeItem.longitude)
                    selectedType.set(placeTypes.first { it.id == placeItem.type })
                })
        compositeDisposable.add(disposable)
    }

    private fun addLocation(name: String?,
                            type: Int,
                            latitude: Double,
                            longitude: Double,
                            isStarred: Boolean) {
        val place = PlaceItem(name, latitude, longitude, type, isStarred)
        if (place.isEmpty()) {
            errorMessage.set(context.getString(R.string.msg_empty_name))
        } else {
            itemDataSource.addPlace(place)
            navigator?.goBackToList()
        }
    }

    private fun updateLocation(name: String,
                               type: Int,
                               latitude: Double,
                               longitude: Double,
                               isStarred: Boolean,
                               id: String) {
        val place = PlaceItem(name, latitude, longitude, type, isStarred, id)
        itemDataSource.updatePlace(place)
        navigator?.goBackToList()
    }
}