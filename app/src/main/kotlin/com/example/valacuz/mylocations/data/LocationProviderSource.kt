package com.example.valacuz.mylocations.data

import android.location.Location
import io.reactivex.Observable

interface LocationProviderSource {

    fun startUpdates()

    fun stopUpdates()

    fun getObservableLocation(): Observable<Location>
}