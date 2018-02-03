package com.example.valacuz.mylocations.list

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.example.valacuz.mylocations.data.PlaceItem
import java.lang.ref.WeakReference

class PlaceItemViewModel(placeItem: PlaceItem) : BaseObservable() {

    // This navigator wrapped in a WeakReference to avoid leaks because it has reference to activity.
    // There's no straightforward way to clear it for each item of adapter
    private var placeItemNavigator: WeakReference<PlaceItemNavigator?>? = null

    private val place: PlaceItem = placeItem

    @Bindable
    fun getName(): String = place.name!!

    @Bindable
    fun getType(): Int = place.type

    @Bindable
    fun getCoordinateString(): String = "%.6f, %.6f".format(place.latitude, place.longitude)

    fun placeClick() {
        placeItemNavigator?.get()?.displayDetail(place.id)
    }

    fun placeLongClick(): Boolean {
        placeItemNavigator?.get()?.displayItemAction(place)
        return true
    }

    fun setNavigator(navigator: PlaceItemNavigator?) {
        placeItemNavigator = WeakReference(navigator)
    }
}