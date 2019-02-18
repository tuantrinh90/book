package com.dz.di

import com.dz.applications.AppContext
import com.dz.commons.Keys
import okhttp3.Interceptor
import okhttp3.Response

class AccessInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
        requestBuilder.addHeader(Keys.AUTHORIZATION, "${AppContext.getTokenType()} ${AppContext.getAccessToken()}")
        return chain.proceed(requestBuilder.build())
    }
}