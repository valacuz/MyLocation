package com.example.valacuz.mylocations.util

import android.content.Context
import android.content.Intent
import android.net.Uri

class NostraMapDisplaySource(context: Context) : MapDisplaySource {

    private val mContext: Context = context.applicationContext

    override fun displayOnMap(latitude: Double, longitude: Double) {
        val uri = Uri.parse("https://map.nostramap.com/NostraMap/?lat=$latitude&lon=$longitude&lev=18")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.resolveActivity(mContext.packageManager)?.apply {
            mContext.startActivity(intent)
        }
    }
}