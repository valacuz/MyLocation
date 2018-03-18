package com.example.valacuz.mylocations.list

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.databinding.ObservableArrayList
import android.databinding.ObservableList
import com.example.valacuz.mylocations.BR
import com.example.valacuz.mylocations.data.PlaceItem
import com.example.valacuz.mylocations.data.repository.PlaceDataSource
import com.example.valacuz.mylocations.util.EspressoIdlingResource
import com.example.valacuz.mylocations.util.schedulers.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable

class PlaceListViewModel(private val itemDataSource: PlaceDataSource,
                         private val schedulerProvider: SchedulerProvider) : BaseObservable() {

    private val compositeDisposable = CompositeDisposable()

    // Bindable values
    val items: ObservableList<PlaceItem> = ObservableArrayList()

    private var navigator: PlaceNavigator? = null

    fun setNavigator(navigator: PlaceNavigator) {
        this.navigator = navigator
    }

    // Called by the data binding library and button click listener.
    fun addNewTask() = navigator?.addLocation()

    @Bindable
    fun isEmpty(): Boolean = items.isEmpty()

    fun start() {
        loadItems()
    }

    fun onActivityDestroyed() {
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
        navigator = null   // Remove navigator references to avoid leaks.
    }

    fun onDeletePlaceClick(place: PlaceItem) {
        itemDataSource.deletePlace(place)
        items.remove(place)
        notifyPropertyChanged(BR.empty) // Update @Bindable
    }

    fun loadItems() {
        // Make espresso knows that app is currently busy.
        EspressoIdlingResource.increment()

        val disposable = itemDataSource.getAllPlaces()
                .observeOn(schedulerProvider.ui())
                .subscribeOn(schedulerProvider.io())
                .subscribe({ places ->
                    if (!EspressoIdlingResource.getIdlingResource().isIdleNow) {
                        EspressoIdlingResource.decrement()  // Set app as idle
                    }
                    items.clear()
                    items.addAll(places)
                    notifyPropertyChanged(BR.empty) // It's a @Bindable so update manually.
                })
        compositeDisposable.add(disposable)
    }
}