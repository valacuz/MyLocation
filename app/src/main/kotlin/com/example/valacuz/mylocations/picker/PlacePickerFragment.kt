package com.example.valacuz.mylocations.picker

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.valacuz.mylocations.databinding.FragmentPlacePickerBinding
import com.google.android.gms.maps.GoogleMap


class PlacePickerFragment : Fragment() {

    private val LOCATION_REQUEST_CODE: Int = 3001

    private lateinit var viewModel: PlacePickerViewModel
    private lateinit var fragmentBinding: FragmentPlacePickerBinding

    private lateinit var googleMap: GoogleMap

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        fragmentBinding = FragmentPlacePickerBinding.inflate(inflater, container, false)
        fragmentBinding.viewModel = viewModel
        return fragmentBinding.root
    }

    @SuppressLint("MissingPermission")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //
        viewModel.create()
        //
        fragmentBinding.mapView.onCreate(savedInstanceState)
        fragmentBinding.mapView.getMapAsync {
            googleMap = it
            // If device location granted, display current location on map.
            if (isLocationPermissionGranted()) {
                googleMap.isMyLocationEnabled = true
                googleMap.uiSettings.isMyLocationButtonEnabled = false
            }
        }
        // Add event listener for pick button
        fragmentBinding.pickButton.setOnClickListener { _ ->
            val target = googleMap.cameraPosition.target
            viewModel.pickLocation(target.latitude, target.longitude)
        }
        // Request location permission.
        if (!isLocationPermissionGranted()) {
            requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_REQUEST_CODE)
        }
    }

    override fun onStart() {
        super.onStart()
        fragmentBinding.mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        fragmentBinding.mapView.onResume()
    }

    override fun onPause() {
        fragmentBinding.mapView.onPause()
        super.onPause()
    }

    override fun onStop() {
        fragmentBinding.mapView.onStop()
        super.onStop()
    }

    override fun onDestroy() {
        fragmentBinding.mapView.onDestroy()
        viewModel.onActivityDestroyed()
        super.onDestroy()
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<out String>,
                                            grantResults: IntArray) {
        when (requestCode) {
            LOCATION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(activity, "Location permission granted.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun setViewModel(viewModel: PlacePickerViewModel) {
        this.viewModel = viewModel
    }

    private fun isLocationPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(activity.applicationContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        fun newInstance(): PlacePickerFragment = PlacePickerFragment()
    }
}
