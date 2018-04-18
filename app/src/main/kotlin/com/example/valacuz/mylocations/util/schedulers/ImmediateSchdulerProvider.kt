package com.example.valacuz.mylocations.util.schedulers

import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

/**
 * Implementation of [BaseSchedulerProviders] making all [Scheduler] execute
 * synchronously so we can easily run assertions in our tests.
 * <p>
 * To archive this, we use [io.reactivex.internal.schedulers.TrampolineScheduler]
 * from the [Schedulers] class.
 */
class ImmediateSchdulerProvider : BaseSchedulerProviders {

    override fun computation(): Scheduler = Schedulers.trampoline()

    override fun io(): Scheduler = Schedulers.trampoline()

    override fun ui(): Scheduler = Schedulers.trampoline()
}