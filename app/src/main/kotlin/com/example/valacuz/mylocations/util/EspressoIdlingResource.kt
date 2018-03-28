package com.example.valacuz.mylocations.util

import android.support.test.espresso.idling.CountingIdlingResource

class EspressoIdlingResource {

    companion object {

        private const val RESOURCE = "GLOBAL"

        private val countingIdlingResource = CountingIdlingResource(RESOURCE)

        fun increment() {
            countingIdlingResource.increment()
        }

        fun decrement() {
            countingIdlingResource.decrement()
        }

        fun getIdlingResource() = countingIdlingResource
    }
}