package com.example.valacuz.mylocations.picker

import android.databinding.BaseObservable
import android.databinding.ObservableField
import android.databinding.ObservableFloat
import com.example.valacuz.mylocations.data.LocationProviderSource
import com.google.android.gms.maps.model.LatLng
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class PlacePickerViewModel(locationSource: LocationProviderSource) : BaseObservable() {

    // Bindable values
    val centerLatLng: ObservableField<LatLng> = ObservableField()

    val zoomLevel: ObservableFloat = ObservableFloat(16F)

    private var navigator: PlacePickerNavigator? = null

    private var compositeDisposable = CompositeDisposable()

    private var hasZoomToLocation: Boolean = false

    private var currentLatitude: Double = 0.0

    private var currentLongitude: Double = 0.0

    private var locationSource = locationSource

    fun setNavigator(navigator: PlacePickerNavigator) {
        this.navigator = navigator
    }

    fun setCenterLocation(latitude: Double, longitude: Double) {
        centerLatLng.set(LatLng(latitude, longitude))
        centerLatLng.notifyChange()
        // Trigger zoom to location flag to disable default location zoom
        hasZoomToLocation = true
    }

    // Called by the data binding library when current location button clicked.
    fun moveToCurrentLocation() {
        setCenterLocation(currentLatitude, currentLongitude)
    }

    // Called by the view (PlacePickerFragment) when pick location button clicked.
    fun pickLocation(latitude: Double, longitude: Double) {
        navigator?.goBackToForm(latitude, longitude)
    }

    fun create() {
        locationSource.startUpdates()
        val disposable = locationSource
                .getObservableLocation()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe { location ->
                    currentLatitude = location.latitude
                    currentLongitude = location.longitude
                    // For first time when location found, center at that location.
                    if (!hasZoomToLocation) {
                        moveToCurrentLocation()
                    }
                }
        compositeDisposable.add(disposable)
    }

    fun onActivityDestroyed() {
        locationSource.stopUpdates()
        compositeDisposable.clear()
        navigator = null   // Remove navigator references to avoid leaks
    }
}