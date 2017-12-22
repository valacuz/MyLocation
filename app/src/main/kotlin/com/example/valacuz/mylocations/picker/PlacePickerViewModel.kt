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

    private var mNavigator: PlacePickerNavigator? = null

    private var mCompositeDisposable = CompositeDisposable()

    private var mHasZoomToLocation: Boolean = false

    private var mCurrentLatitude: Double = 0.0

    private var mCurrentLongitude: Double = 0.0

    private var mLocationSource = locationSource

    fun setNavigator(navigator: PlacePickerNavigator) {
        mNavigator = navigator
    }

    fun setCenterLocation(latitude: Double, longitude: Double) {
        centerLatLng.set(LatLng(latitude, longitude))
        centerLatLng.notifyChange()
        // Trigger zoom to location flag to disable default location zoom
        mHasZoomToLocation = true
    }

    // Called by the data binding library when current location button clicked.
    fun moveToCurrentLocation() {
        setCenterLocation(mCurrentLatitude, mCurrentLongitude)
    }

    // Called by the view (PlacePickerFragment) when pick location button clicked.
    fun pickLocation(latitude: Double, longitude: Double) {
        mNavigator?.goBackToForm(latitude, longitude)
    }

    fun create() {
        mLocationSource.startUpdates()
        val disposable = mLocationSource
                .getObservableLocation()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe { location ->
                    mCurrentLatitude = location.latitude
                    mCurrentLongitude = location.longitude
                    // For first time when location found, center at that location.
                    if (!mHasZoomToLocation) {
                        moveToCurrentLocation()
                    }
                }
        mCompositeDisposable.add(disposable)
    }

    fun onActivityDestroyed() {
        mLocationSource.stopUpdates()
        mCompositeDisposable.clear()
        mNavigator = null   // Remove navigator references to avoid leaks
    }
}