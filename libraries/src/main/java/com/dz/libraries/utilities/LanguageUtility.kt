package com.dz.libraries.utilities

import android.app.Activity
import com.dz.libraries.loggers.Logger
import java.util.*

@Suppress("DEPRECATION")
class LanguageUtility {
    companion object {
        private const val TAG = "LanguageUtility"

        fun configLanguage(activity: Activity, language: String) {
            try {
                val res = activity.resources
                val config = res.configuration
                config.locale = Locale(language)
                res.updateConfiguration(config, res.displayMetrics)
            } catch (e: Exception) {
                Logger.e(TAG, e)
            }
        }
    }
}