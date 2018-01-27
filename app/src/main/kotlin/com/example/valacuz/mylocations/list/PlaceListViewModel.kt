package com.example.valacuz.mylocations.list

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.databinding.ObservableArrayList
import android.databinding.ObservableList
import com.example.valacuz.mylocations.BR
import com.example.valacuz.mylocations.data.PlaceDataSource
import com.example.valacuz.mylocations.data.PlaceItem
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class PlaceListViewModel(itemDataSource: PlaceDataSource) : BaseObservable() {

    // Bindable values
    val items: ObservableList<PlaceItem> = ObservableArrayList()

    private val mItemDataSource: PlaceDataSource = itemDataSource

    private var mNavigator: PlaceNavigator? = null

    fun setNavigator(navigator: PlaceNavigator) {
        mNavigator = navigator
    }

    // Called by the data binding library and button click listener.
    fun addNewTask() = mNavigator?.addLocation()

    @Bindable
    fun isEmpty(): Boolean = items.isEmpty()

    fun start() {
        loadItems()
    }

    fun onActivityDestroyed() {
        mNavigator = null   // Remove navigator references to avoid leaks.
    }

    fun onDeletePlaceClick(place: PlaceItem) {
        mItemDataSource.deletePlace(place)
        items.remove(place)
        notifyPropertyChanged(BR.empty) // Update @Bindable
    }

    fun loadItems() {
        mItemDataSource.getAllPlaces()
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe({ places ->
                    items.clear()
                    items.addAll(places)
                    notifyPropertyChanged(BR.empty) // It's a @Bindable so update manually.
                })
    }
}