package com.example.valacuz.mylocations.util

import io.reactivex.FlowableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

interface ScheduleStrategy {

    fun <T> applySchedule(): FlowableTransformer<T, T>
}

class DefaultScheduleStrategy : ScheduleStrategy {

    override fun <T> applySchedule(): FlowableTransformer<T, T> {
        return FlowableTransformer {
            it.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
        }
    }
}