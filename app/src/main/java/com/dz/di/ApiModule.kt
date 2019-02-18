package com.dz.di

import android.annotation.SuppressLint
import com.dz.interactors.services.IApiService
import com.dz.interactors.services.IFileApiService
import com.dz.interactors.services.ILongApiService
import com.dz.libraries.utilities.GsonUtility
import com.dz.utilities.ServiceConfig
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSession
import javax.net.ssl.X509TrustManager

@Module
class ApiModule {
    companion object {
        fun addTrustCertificate(client: OkHttpClient.Builder) {
            // trust all certificate
            val tm = arrayOf(object : X509TrustManager {
                @SuppressLint("TrustAllX509TrustManager")
                @Throws(CertificateException::class)
                override fun checkClientTrusted(xcs: Array<X509Certificate>, string: String) {
                }

                @SuppressLint("TrustAllX509TrustManager")
                @Throws(CertificateException::class)
                override fun checkServerTrusted(xcs: Array<X509Certificate>, string: String) {
                }

                override fun getAcceptedIssuers(): Array<X509Certificate>? {
                    return arrayOf()
                }
            })

            // Install the all-trusting trust manager
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, tm, java.security.SecureRandom())
            // Create an ssl socket factory with our all-trusting manager
            val sslSocketFactory = sslContext.socketFactory

            // certificate
            client.sslSocketFactory(sslSocketFactory, tm[0] as X509TrustManager)
            client.hostnameVerifier(object : HostnameVerifier {
                @SuppressLint("BadHostnameVerifier")
                override fun verify(hostname: String, session: SSLSession): Boolean {
                    return true
                }
            })
        }
    }

    enum class OkHttpType { USUAL, LONG_TIMEOUT, FILE }

    fun addLogInterceptorIfNeed(logging: HttpLoggingInterceptor?, client: OkHttpClient.Builder) {
        client.addInterceptor(logging!!)
    }

    fun provideBaseOkHttp(logging: HttpLoggingInterceptor?, access: AccessInterceptor,
                          type: OkHttpType): OkHttpClient {
        val client = OkHttpClient.Builder()
                .retryOnConnectionFailure(ServiceConfig.RETRY_POLICY)
                .addInterceptor(access)

        // add log
        addLogInterceptorIfNeed(logging, client)

        // time timeout
        val reqTimeOut = when (type) {
            OkHttpType.FILE -> {
                ServiceConfig.REQUEST_FILE_TIMEOUT
            }
            OkHttpType.LONG_TIMEOUT -> {
                ServiceConfig.REQUEST_TIMEOUT_LONG
            }
            OkHttpType.USUAL -> {
                ServiceConfig.REQUEST_TIMEOUT
            }
        }

        // trust certificate
        addTrustCertificate(client)

        // timeout
        client.connectTimeout(reqTimeOut.toLong(), TimeUnit.SECONDS)
                .readTimeout(reqTimeOut.toLong(), TimeUnit.SECONDS)
                .writeTimeout(reqTimeOut.toLong(), TimeUnit.SECONDS)

        return client.build()
    }

    fun provideBaseRetrofit(baseUrl: String, converterFactory: GsonConverterFactory, client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(converterFactory)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build()
    }

    @Singleton
    @Provides
    fun provideAccessInterceptor(): AccessInterceptor {
        return AccessInterceptor()
    }

    @Singleton
    @Provides
    fun provideRetrofit(converterFactory: GsonConverterFactory, client: OkHttpClient): Retrofit {
        return provideBaseRetrofit(ServiceConfig.BASE_URL, converterFactory, client)
    }

    @Singleton
    @Provides
    fun provideOkHttp(logging: HttpLoggingInterceptor, access: AccessInterceptor): OkHttpClient {
        return provideBaseOkHttp(logging, access, OkHttpType.USUAL)
    }

    @Singleton
    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Singleton
    @Provides
    fun provideGsonConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create(GsonUtility.getGsonBuilder())
    }

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): IApiService {
        return retrofit.create(IApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideFileService(logging: HttpLoggingInterceptor, access: AccessInterceptor): IFileApiService {
        val retrofit = provideBaseRetrofit(ServiceConfig.BASE_URL, provideGsonConverterFactory(), provideBaseOkHttp(logging, access, OkHttpType.FILE))
        return retrofit.create(IFileApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideLongService(logging: HttpLoggingInterceptor, access: AccessInterceptor): ILongApiService {
        val client = provideBaseOkHttp(logging, access, OkHttpType.LONG_TIMEOUT)
        val retrofit = provideBaseRetrofit(ServiceConfig.BASE_URL, provideGsonConverterFactory(), client)
        return retrofit.create(ILongApiService::class.java)
    }
}
