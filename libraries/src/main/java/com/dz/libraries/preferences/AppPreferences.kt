package com.dz.libraries.preferences

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.dz.libraries.loggers.Logger
import com.google.gson.Gson
import kotlin.reflect.KClass

object AppPreferences {
    private const val TAG = "AppPreferences"

    // instance of share preferences
    private lateinit var prefsShared: SharedPreferences

    /**
     * init function
     */
    fun init(application: Application) {
        prefsShared = application.getSharedPreferences(application.packageName, Context.MODE_PRIVATE)
    }

    /**
     * save value to shared preferences
     */
    private fun edit(operator: (SharedPreferences.Editor) -> Unit) {
        try {
            val prefsEditor = prefsShared.edit()
            operator(prefsEditor)
            prefsEditor.apply()
        } catch (e: Exception) {
            Logger.e(TAG, e)
        }
    }

    /**
     * set value to shared preferences
     */
    fun setValue(key: String, value: Any) {
        when (value) {
            is String -> edit { it.putString(key, value) }
            is Int -> edit { it.putInt(key, value) }
            is Long -> edit { it.putLong(key, value) }
            is Float -> edit { it.putFloat(key, value) }
            is Boolean -> edit { it.putBoolean(key, value) }
            else -> UnsupportedOperationException("Not yet implemented!!!")
        }
    }

    fun setValueJson(key: String, value: Any) {
        edit { it.putString(key, Gson().toJson(value)) }
    }

    /**
     * get value from shared preferences
     */
    @Suppress("UNCHECKED_CAST")
    fun <T> getValue(key: String, clazz: KClass<*>, defaultValue: T? = null): T? {
        return when (clazz) {
            String::class -> prefsShared.getString(key, defaultValue as? String) as? T?
            Int::class -> prefsShared.getInt(key, defaultValue as? Int ?: -1) as? T?
            Long::class -> prefsShared.getLong(key, defaultValue as? Long ?: -1) as? T?
            Float::class -> prefsShared.getFloat(key, defaultValue as? Float ?: -1f) as? T?
            Boolean::class -> prefsShared.getBoolean(key, defaultValue as? Boolean ?: false) as? T?
            else -> throw UnsupportedOperationException("Not yet implemented")
        }
    }

    /**
     * clear value in shared preferences
     */
    fun clear() = prefsShared.edit().clear().commit()
}