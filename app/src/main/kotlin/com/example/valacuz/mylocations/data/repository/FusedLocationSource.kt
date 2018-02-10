package com.example.valacuz.mylocations.data.repository

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import android.support.v4.content.ContextCompat
import com.example.valacuz.mylocations.data.LocationProviderSource
import com.google.android.gms.location.*
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class FusedLocationSource private constructor(context: Context) : LocationProviderSource {

    private var locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            publishSubject.onNext(locationResult!!.lastLocation)
        }
    }

    private var locationClient: FusedLocationProviderClient = LocationServices
            .getFusedLocationProviderClient(context)

    private var locationRequest: LocationRequest = LocationRequest.create()
            .setInterval(5000)
            .setFastestInterval(2500)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)

    private var publishSubject: PublishSubject<Location> = PublishSubject.create()

    override fun getObservableLocation(): Observable<Location> = publishSubject

    override fun startUpdates() {
        val isPermissionGranted = ContextCompat.checkSelfPermission(locationClient.applicationContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

        if (isPermissionGranted) {
            locationClient.requestLocationUpdates(
                    locationRequest, locationCallback, Looper.myLooper())
        }
    }

    override fun stopUpdates() {
        locationClient.removeLocationUpdates(locationCallback)
    }

    companion object {

        @Volatile
        private var INSTANCE: FusedLocationSource? = null

        fun getInstance(context: Context): FusedLocationSource =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: FusedLocationSource(context.applicationContext)
                }
    }
}