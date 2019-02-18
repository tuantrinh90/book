package com.dz.models

data class ErrorBody(var status: Int = 0, override var message: String? = null) : Throwable()
