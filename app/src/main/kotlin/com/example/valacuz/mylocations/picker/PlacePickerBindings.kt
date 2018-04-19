package com.example.valacuz.mylocations.picker

import android.databinding.BindingAdapter
import android.graphics.Camera
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng

object PlacePickerBindings {

    @BindingAdapter(value = ["center_location", "zoom_level"])
    @JvmStatic
    fun centerAt(mapView: MapView, latLng: LatLng?, zoomLevel: Float) {
        if (latLng != null) {
            mapView.getMapAsync { map ->
                // To prevent infinite loop, make sure coordinate was changed.
                val mapLatLng = map.cameraPosition.target
                if (latLng.latitude != mapLatLng.latitude ||
                        latLng.longitude != mapLatLng.longitude) {
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel))
                }
            }
        }
    }
}