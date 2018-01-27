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

    private var mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            mPublishSubject.onNext(locationResult!!.lastLocation)
        }
    }

    private var mLocationClient: FusedLocationProviderClient = LocationServices
            .getFusedLocationProviderClient(context)

    private var mLocationRequest: LocationRequest = LocationRequest.create()
            .setInterval(5000)
            .setFastestInterval(2500)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)

    private var mPublishSubject: PublishSubject<Location> = PublishSubject.create()

    override fun getObservableLocation(): Observable<Location> = mPublishSubject

    override fun startUpdates() {
        val isPermissionGranted = ContextCompat.checkSelfPermission(mLocationClient.applicationContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

        if (isPermissionGranted) {
            mLocationClient.requestLocationUpdates(
                    mLocationRequest, mLocationCallback, Looper.myLooper())
        }
    }

    override fun stopUpdates() {
        mLocationClient.removeLocationUpdates(mLocationCallback)
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