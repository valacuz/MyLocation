package com.example.valacuz.mylocations.data.repository

import android.location.Location
import io.reactivex.Observable

interface LocationProviderSource {

    fun startUpdates()

    fun stopUpdates()

    fun getObservableLocation(): Observable<Location>
}