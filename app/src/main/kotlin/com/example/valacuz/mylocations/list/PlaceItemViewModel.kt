package com.example.valacuz.mylocations.list

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.example.valacuz.mylocations.data.PlaceItem
import java.lang.ref.WeakReference

class PlaceItemViewModel(placeItem: PlaceItem) : BaseObservable() {

    // This navigator wrapped in a WeakReference to avoid leaks because it has reference to activity.
    // There's no straightforward way to clear it for each item of adapter
    private var mNavigator: WeakReference<PlaceItemNavigator?>? = null

    private val mPlace: PlaceItem = placeItem

    @Bindable
    fun getName(): String = mPlace.name

    @Bindable
    fun getType(): Int = mPlace.type

    @Bindable
    fun getCoordinateString(): String = "%.6f, %.6f".format(mPlace.latitude, mPlace.longitude)

    fun placeClick() {
        mNavigator?.get()?.displayDetail(mPlace.id)
    }

    fun placeLongClick(): Boolean {
        mNavigator?.get()?.displayItemAction(mPlace)
        return true
    }

    fun setNavigator(navigator: PlaceItemNavigator?) {
        mNavigator = WeakReference(navigator)
    }
}