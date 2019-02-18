package com.dz.libraries.utilities

class CollectionUtility<T> {
    companion object {
        fun <T> with(vararg collection: T?) = CollectionUtility(collection)
        fun <T> with(collection: List<T>?) = CollectionUtility(collection)

        fun <T> isNullOrEmpty(vararg collection: T?): Boolean = collection.isEmpty()
        fun <T> isNullOrEmpty(collection: List<T>? = null): Boolean = collection == null || collection.isEmpty()
    }

    private val collections: List<T>?

    private constructor(vararg c: T) {
        collections = listOf(*c)
    }

    private constructor(c: List<T>?) {
        collections = c
    }

    fun size(): Int = if (isNullOrEmpty(collections)) 0 else collections!!.size

    fun doIfEmpty(consumer: () -> Unit): CollectionUtility<T> {
        if (isNullOrEmpty(collections)) {
            consumer()
        }

        return this
    }

    fun doIfPresent(consumer: (collections: List<T>) -> Unit): CollectionUtility<T> {
        if (!isNullOrEmpty(collections)) {
            consumer(collections!!)
        }

        return this
    }

    fun get(): List<T>? = collections

    fun get(defaultCollections: List<T>?) = if (isNullOrEmpty(collections)) defaultCollections else collections
}