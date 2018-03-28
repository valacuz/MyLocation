package com.example.valacuz.mylocations.util.schedulers

import io.reactivex.Scheduler

interface BaseSchedulerProviders {

    fun computation(): Scheduler

    fun io(): Scheduler

    fun ui(): Scheduler
}