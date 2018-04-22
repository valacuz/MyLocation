package com.example.valacuz.mylocations.util

import android.support.test.espresso.IdlingResource
import java.util.concurrent.atomic.AtomicInteger

/**
 * This class clones [android.support.test.espresso.idling.CountingIdlingResource]
 * because the original one cannot be tested in plain JUnit due to TextUtils.isEmpty().
 * So I decide to re-write it in kotlin and make it testable on plain JUnit.
 */
class CountingIdlingResource(private val resourceName: String) : IdlingResource {

    @Volatile
    private var resourceCallback: IdlingResource.ResourceCallback? = null

    private val counter: AtomicInteger = AtomicInteger(0)

    init {
        if (resourceName.isEmpty()) {
            throw IllegalArgumentException("resourceName cannot be null or empty.")
        }
    }

    override fun getName(): String = resourceName

    override fun isIdleNow(): Boolean = counter.get() == 0

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
        this.resourceCallback = callback
    }

    fun increment() {
        counter.getAndIncrement()
    }

    fun decrement() {
        val counterVal = counter.decrementAndGet()
        if (counterVal == 0) {
            resourceCallback?.onTransitionToIdle()
        }
    }
}