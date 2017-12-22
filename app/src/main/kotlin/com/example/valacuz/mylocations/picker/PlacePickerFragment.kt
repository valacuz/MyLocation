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

    private val LOCATION_REQUEST_CODE: Int = 10001

    private lateinit var mViewModel: PlacePickerViewModel
    private lateinit var mFragmentBinding: FragmentPlacePickerBinding

    private lateinit var mGoogleMap: GoogleMap

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        mFragmentBinding = FragmentPlacePickerBinding.inflate(inflater, container, false)
        mFragmentBinding.viewModel = mViewModel
        return mFragmentBinding.root
    }

    @SuppressLint("MissingPermission")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //
        mViewModel.create()
        //
        mFragmentBinding.mapView.onCreate(savedInstanceState)
        mFragmentBinding.mapView.getMapAsync {
            mGoogleMap = it
            // If device location granted, display current location on map.
            if (isLocationPermissionGranted()) {
                mGoogleMap.isMyLocationEnabled = true
                mGoogleMap.uiSettings.isMyLocationButtonEnabled = false
            }
        }
        // Add event listener for pick button
        mFragmentBinding.pickButton.setOnClickListener { _ ->
            val target = mGoogleMap.cameraPosition.target
            mViewModel.pickLocation(target.latitude, target.longitude)
        }
        // Request location permission.
        if (!isLocationPermissionGranted()) {
            requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_REQUEST_CODE)
        }
    }

    override fun onStart() {
        super.onStart()
        mFragmentBinding.mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mFragmentBinding.mapView.onResume()
    }

    override fun onPause() {
        mFragmentBinding.mapView.onPause()
        super.onPause()
    }

    override fun onStop() {
        mFragmentBinding.mapView.onStop()
        super.onStop()
    }

    override fun onDestroy() {
        mFragmentBinding.mapView.onDestroy()
        mViewModel.onActivityDestroyed()
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
        mViewModel = viewModel
    }

    private fun isLocationPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(activity.applicationContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        fun newInstance(): PlacePickerFragment = PlacePickerFragment()
    }
}
