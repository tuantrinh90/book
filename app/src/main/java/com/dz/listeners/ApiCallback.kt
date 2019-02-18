package com.dz.listeners

abstract class ApiCallback<T> : IApiCallback<T> {
    override fun onStart() {}

    override fun onFinish() {}
}
