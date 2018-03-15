package com.example.valacuz.mylocations.di.module

import android.content.Context
import com.example.valacuz.mylocations.domain.display.GoogleMapDisplaySource
import com.example.valacuz.mylocations.domain.share.GoogleMapShareSource
import dagger.Module
import dagger.Provides

@Module
class PlaceMenuModule(private val context: Context) {

    @Provides
    fun provideMapDisplay() = GoogleMapDisplaySource(context)

    @Provides
    fun provideShareSource() = GoogleMapShareSource(context)
}