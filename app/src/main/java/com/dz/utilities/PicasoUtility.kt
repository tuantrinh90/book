package com.dz.utilities

import com.dz.di.ApiModule
import com.dz.libraries.applications.ExtApplication
import com.dz.libraries.utilities.StringUtility
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

object PicasoUtility {
    fun get(): Picasso {
        // okhttpclient
        val client = OkHttpClient.Builder()
                .retryOnConnectionFailure(ServiceConfig.RETRY_POLICY)
        client.connectTimeout(ServiceConfig.REQUEST_TIMEOUT_LONG.toLong(), TimeUnit.SECONDS)
                .readTimeout(ServiceConfig.REQUEST_TIMEOUT_LONG.toLong(), TimeUnit.SECONDS)
                .writeTimeout(ServiceConfig.REQUEST_TIMEOUT_LONG.toLong(), TimeUnit.SECONDS)

        // trust all certificate
        ApiModule.addTrustCertificate(client)

        // picaso
        val builder = Picasso.Builder(ExtApplication.instance)
        val okHttpDownloader = OkHttp3Downloader(client.build())
        builder.downloader(okHttpDownloader)
        return builder.build()
    }

    fun getLink(path: String?): String? = if (StringUtility.isNullOrEmpty(path)) null else path
}