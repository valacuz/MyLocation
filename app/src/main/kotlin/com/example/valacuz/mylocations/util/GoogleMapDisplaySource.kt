package com.example.valacuz.mylocations.util

import android.content.Context
import android.content.Intent
import android.net.Uri

class GoogleMapDisplaySource(context: Context) : MapDisplaySource {

    private val mContext: Context = context.applicationContext  // Force application context.

    override fun displayOnMap(latitude: Double, longitude: Double) {
        val uri = Uri.parse("geo:$latitude,$longitude?z=18")
        val intent = Intent(Intent.ACTION_VIEW, uri)
                .setPackage("com.google.android.apps.maps")
        intent.resolveActivity(mContext.packageManager)?.apply {
            mContext.startActivity(intent)
        }
    }
}