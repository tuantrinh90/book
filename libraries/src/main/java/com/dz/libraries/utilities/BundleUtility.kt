package com.dz.libraries.utilities

import android.os.Bundle
import android.os.Parcelable
import java.io.Serializable

class BundleUtility private constructor(private var bundle: Bundle? = null) {
    companion object {
        private const val TAG = "BundleUtility"

        fun newInstance(vararg pairs: Pair<String, Any>): Bundle {
            val bundle = Bundle()
            pairs.forEach {
                println("$TAG::newInstance::it:: $it")
                when (it.second) {
                    is Boolean -> bundle.putBoolean(it.first, it.second as Boolean)
                    is Byte -> bundle.putByte(it.first, it.second as Byte)
                    is Char -> bundle.putChar(it.first, it.second as Char)
                    is Short -> bundle.putShort(it.first, it.second as Short)
                    is Int -> bundle.putInt(it.first, it.second as Int)
                    is Long -> bundle.putLong(it.first, it.second as Long)
                    is Float -> bundle.putFloat(it.first, it.second as Float)
                    is Double -> bundle.putDouble(it.first, it.second as Double)
                    is String -> bundle.putString(it.first, it.second as String)
                    is CharSequence -> bundle.putCharSequence(it.first, it.second as CharSequence)
                    is Parcelable -> bundle.putParcelable(it.first, it.second as Parcelable)
                    is Serializable -> bundle.putSerializable(it.first, it.second as Serializable)
                    is Bundle -> bundle.putBundle(it.first, it.second as Bundle)
                    else -> throw UnsupportedOperationException("Does not support $it")
                }
            }
            return bundle
        }

        fun isNullOrEmpty(bundle: Bundle? = null) = bundle == null || bundle.isEmpty

        fun with(bundle: Bundle? = null) = BundleUtility(bundle)
    }

    fun doIfEmpty(consumer: () -> Unit): BundleUtility {
        if (isNullOrEmpty(bundle)) consumer()
        return this
    }

    fun doIfPresent(consumer: (Bundle) -> Unit): BundleUtility {
        if (!isNullOrEmpty(bundle)) consumer(bundle!!)
        return this
    }
}