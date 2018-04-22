package com.example.valacuz.mylocations.form

import android.content.Context
import com.example.valacuz.mylocations.R

class ResourcePlaceFormMessageProvider(private val context: Context)
    : PlaceFormMessageProvider {

    override fun getErrorEmptyName(): String = context.getString(R.string.msg_empty_name)
}