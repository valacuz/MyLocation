package com.example.valacuz.mylocations.domain.share

import android.content.Context
import android.content.Intent

class NostraMapShareSource(context: Context) : ShareContentSource {

    private val context: Context = context.applicationContext

    override fun shareContent(name: String, latitude: Double, longitude: Double) {
        val url = "https://map.nostramap.com/NostraMap/?lat=$latitude&lon=$longitude&lev=18"
        val intent = Intent(Intent.ACTION_SEND)
                .setType("text/plain")
                .putExtra(Intent.EXTRA_SUBJECT, name)
                .putExtra(Intent.EXTRA_TEXT, url)
        context.startActivity(Intent.createChooser(intent, "Share via"))
    }
}