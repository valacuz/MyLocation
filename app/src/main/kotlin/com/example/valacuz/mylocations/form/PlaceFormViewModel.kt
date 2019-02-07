package com.example.valacuz.mylocations.form

import android.databinding.*
import com.example.valacuz.mylocations.data.PlaceItem
import com.example.valacuz.mylocations.data.PlaceType
import com.example.valacuz.mylocations.data.repository.PlaceDataSource
import com.example.valacuz.mylocations.data.repository.PlaceTypeDataSource
import com.example.valacuz.mylocations.util.EspressoIdlingResource
import com.example.valacuz.mylocations.util.schedulers.BaseSchedulerProviders
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable

class PlaceFormViewModel(private val messageProvider: PlaceFormMessageProvider,
                         private val itemDataSource: PlaceDataSource,
                         private val typeDataSource: PlaceTypeDataSource,
                         private val schedulerProvider: BaseSchedulerProviders,
                         id: String? = null)
    : BaseObservable() {

    // Binding fields.
    val picturePath: ObservableField<String?> = ObservableField()

    val name: ObservableField<String> = ObservableField()

    val latitude: ObservableDouble = ObservableDouble(0.0)

    val longitude: ObservableDouble = ObservableDouble(0.0)

    val selectedType: ObservableField<PlaceType> = ObservableField()

    val starred: ObservableBoolean = ObservableBoolean(false)

    // Used to display error messages
    val errorMessage: ObservableField<String?> = ObservableField()

    // Used to display coordinate in format {lat}, {lon}
    val coordinateString: ObservableField<String> = ObservableField()

    // Used to binding with spinner for types of place
    val placeTypes: ObservableList<PlaceType> = ObservableArrayList<PlaceType>()

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
            addLocation(name.get(), selectedType.get()!!.id,
                    latitude.get(), longitude.get(), starred.get(), picturePath.get())
        } else {
            updateLocation(name.get()!!, selectedType.get()!!.id,
                    latitude.get(), longitude.get(), starred.get(), picturePath.get(), placeId!!)
        }
    }

    fun locationTextClick() {
        navigator?.dispatchPickLocation(latitude.get(), longitude.get())
    }

    fun pictureClick() {
        navigator?.dispatchTakePicture()
    }

    fun setCoordinate(lat: Double, lon: Double) {
        latitude.set(lat)
        longitude.set(lon)
        coordinateString.set("%.6f, %.6f".format(latitude.get(), longitude.get()))
    }

    private fun isNewLocation(): Boolean = placeId == null

    private fun populatePlaceType() {
        // Make espresso knows that app currently busy.
        EspressoIdlingResource.increment()

        val disposable = typeDataSource.getAllTypes()
                .observeOn(schedulerProvider.ui())
                .subscribeOn(schedulerProvider.io())
                .subscribe { types ->
                    placeTypes.addAll(types)

                    if (!EspressoIdlingResource.getIdlingResource().isIdleNow) {
                        EspressoIdlingResource.decrement()
                    }
                }
        compositeDisposable.add(disposable)
    }

    private fun populateItem(placeId: String) {
        // Make espresso knows that app currently busy.
        EspressoIdlingResource.increment()

        val disposable = itemDataSource.getById(placeId)
                .observeOn(schedulerProvider.ui())
                .subscribeOn(schedulerProvider.io())
                .subscribe { placeItem ->
                    picturePath.set(placeItem.picturePath)
                    name.set(placeItem.name)
                    starred.set(placeItem.isStarred)
                    setCoordinate(placeItem.latitude, placeItem.longitude)
                    selectedType.set(placeTypes.first { it.id == placeItem.type })

                    if (!EspressoIdlingResource.getIdlingResource().isIdleNow) {
                        EspressoIdlingResource.decrement()
                    }
                }
        compositeDisposable.add(disposable)
    }

    private fun addLocation(name: String?,
                            type: Int,
                            latitude: Double,
                            longitude: Double,
                            isStarred: Boolean,
                            picturePath: String?) {
        val place = PlaceItem(name, latitude, longitude, type, isStarred, picturePath)
        if (place.isEmpty()) {
            errorMessage.set(messageProvider.getErrorEmptyName())
        } else {
            // Mark as busy
            EspressoIdlingResource.increment()

            val disposable = Completable.fromAction { itemDataSource.addPlace(place) }
                    .observeOn(schedulerProvider.ui())
                    .subscribeOn(schedulerProvider.io())
                    .subscribe {
                        if (!EspressoIdlingResource.getIdlingResource().isIdleNow) {
                            EspressoIdlingResource.decrement()
                        }
                        navigator?.dispatchToPlaceList()
                    }
            compositeDisposable.add(disposable)
        }
    }

    private fun updateLocation(name: String,
                               type: Int,
                               latitude: Double,
                               longitude: Double,
                               isStarred: Boolean,
                               picturePath: String?,
                               id: String) {
        val place = PlaceItem(name, latitude, longitude, type, isStarred, picturePath, id)
        if (place.isEmpty()) {
            errorMessage.set(messageProvider.getErrorEmptyName())
        } else {
            // Mark as busy
            EspressoIdlingResource.increment()

            val disposable = Completable.fromAction { itemDataSource.updatePlace(place) }
                    .observeOn(schedulerProvider.ui())
                    .subscribeOn(schedulerProvider.io())
                    .subscribe {
                        if (!EspressoIdlingResource.getIdlingResource().isIdleNow) {
                            EspressoIdlingResource.decrement()
                        }
                        navigator?.dispatchToPlaceList()
                    }
            compositeDisposable.add(disposable)
        }
    }
}