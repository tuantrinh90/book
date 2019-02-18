package com.dz.listeners

interface IApiCallback<T> {
    fun onStart()

    fun onFinish()

    fun onSuccess(t: T?)

    fun onError(error: String?)
}