package com.dz.libraries.utilities

import android.app.ActivityManager
import android.content.Context
import com.dz.libraries.loggers.Logger

@Suppress("DEPRECATION")
class ServiceUtility {
    companion object {
        private const val TAG = "ServiceUtility"

        fun getAllRunningService(context: Context): Set<String>? {
            try {
                val names = HashSet<String>()

                val activityManager: ActivityManager = context.applicationContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
                val runningServiceInfos = activityManager.getRunningServices(0x7FFFFFFF)
                if (CollectionUtility.isNullOrEmpty(runningServiceInfos)) return null

                for (runningServiceInfo: ActivityManager.RunningServiceInfo in runningServiceInfos) {
                    names.add(runningServiceInfo.service.className)
                }

                return names
            } catch (e: Exception) {
                Logger.e(TAG, e)
            }

            return null
        }

        fun isRunningService(context: Context, className: String): Boolean {
            try {
                val names = getAllRunningService(context)
                if (CollectionUtility.isNullOrEmpty(names)) return false

                return names!!.any { it.contains(className, true) }
            } catch (e: Exception) {
                Logger.e(TAG, e)
            }

            return false
        }
    }
}