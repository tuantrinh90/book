package com.dz.libraries.utilities

class OptionalUtility<T> private constructor(v: T?) {
    companion object {
        fun <T> with(value: T?): OptionalUtility<T> = OptionalUtility(value)

        fun <T> isNullOrEmpty(value: T?): Boolean = value == null
    }

    val value: T? = v

    fun doIfEmpty(consumer: () -> Unit): OptionalUtility<T> {
        if (isNullOrEmpty(value)) {
            consumer()
        }

        return this
    }

    fun doIfPresent(consumer: (t: T) -> Unit): OptionalUtility<T> {
        if (!isNullOrEmpty(value)) {
            consumer(value!!)
        }

        return this
    }

    fun get(): T? = value

    fun get(defaultVal: T?): T? = value ?: defaultVal
}