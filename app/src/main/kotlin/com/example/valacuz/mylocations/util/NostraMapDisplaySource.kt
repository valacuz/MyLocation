package com.example.valacuz.mylocations.util

import android.content.Context
import android.content.Intent
import android.net.Uri

class NostraMapDisplaySource(context: Context) : MapDisplaySource {

    private val context: Context = context.applicationContext

    override fun displayOnMap(latitude: Double, longitude: Double) {
        val uri = Uri.parse("https://map.nostramap.com/NostraMap/?lat=$latitude&lon=$longitude&lev=18")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.resolveActivity(context.packageManager)?.apply {
            context.startActivity(intent)
        }
    }
}