package com.example.valacuz.mylocations

/**
 * From: https://medium.com/@BladeCoder/kotlin-singletons-with-argument-194ef06edd9e
 */
open class SingletonHolder<out T, in A>(creator: (A) -> (T)) {

    private var creator: ((A) -> (T))? = creator

    @Volatile private var instance: T? = null

    fun getInstance(arg: A): T {
        val i = instance
        if (i != null) {
            return i
        }
        return synchronized(this) {
            // double-checked locking for thread-safe initialization.
            val i2 = instance
            if (i2 != null) {
                i2
            } else {
                val created = creator!!(arg)    // arg as method signature for T class.
                instance = created
                creator = null  // destroy reference so GC can collect it.
                created
            }
        }
    }
}