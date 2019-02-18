package com.dz.libraries.utilities

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import com.dz.libraries.loggers.Logger
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.*

class NetworkUtility {
    companion object {
        private const val TAG = "NetworkUtility"

        fun isNetworkAvailable(context: Context): Boolean {
            try {
                val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val networkInfo = connectivityManager.activeNetworkInfo
                return networkInfo != null && networkInfo.isConnected
            } catch (e: Exception) {
                Logger.e(TAG, e)
            }

            return false
        }

        fun isNetworkAvailable(context: Context, consumer: () -> Unit) {
            if (isNetworkAvailable(context)) consumer()
        }

        /**
         * @return ssl socket factory
         */
        fun getX509TrustManager(): X509TrustManager {
            return object : X509TrustManager {
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
            }
        }

        /**
         * @return ssl socket factory
         */
        fun getSSLSocketFactory(): SSLSocketFactory? {
            var sslSocketFactory: SSLSocketFactory? = null

            try {
                val tm = getX509TrustManager()
                val sslContext = SSLContext.getInstance("SSL")
                sslContext.init(null, arrayOf<TrustManager>(tm), SecureRandom())
                sslSocketFactory = sslContext.socketFactory
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            } catch (e: KeyManagementException) {
                e.printStackTrace()
            }

            return sslSocketFactory
        }

        /**
         * ignore certificate network
         */
        @Suppress("UNUSED_ANONYMOUS_PARAMETER")
        fun nuke() {
            try {
                HttpsURLConnection.setDefaultSSLSocketFactory(getSSLSocketFactory())
                HttpsURLConnection.setDefaultHostnameVerifier { hostname, session -> true }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}