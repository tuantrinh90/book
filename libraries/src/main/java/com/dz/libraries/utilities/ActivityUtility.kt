package com.dz.libraries.utilities

import android.app.Activity
import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.provider.Settings
import android.view.Surface
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.dz.libraries.loggers.Logger

@Suppress("DEPRECATION")
class ActivityUtility {
    companion object {
        private const val TAG = "ActivityUtility"

        fun isFinish(activity: Activity?): Boolean = activity == null || activity.isFinishing

        fun startActivity(context: Context, clazz: Class<*>, bundle: Bundle? = null) {
            try {
                val intent = Intent(context, clazz)
                bundle?.let { intent.putExtras(bundle) }
                context.startActivity(intent)
            } catch (e: Exception) {
                Logger.e(TAG, e)
            }
        }

        fun startActivityForResult(context: Activity, clazz: Class<*>, requestCode: Int = 0, bundle: Bundle? = null) {
            try {
                val intent = Intent(context, clazz)
                bundle?.let { intent.putExtras(bundle) }
                context.startActivityForResult(intent, requestCode, bundle)
            } catch (e: Exception) {
                Logger.e(TAG, e)
            }
        }

        fun startActivityForResult(context: Fragment, clazz: Class<*>, requestCode: Int = 0, bundle: Bundle? = null) {
            try {
                val intent = Intent(context.context, clazz)
                bundle?.let { intent.putExtras(bundle) }
                context.startActivityForResult(intent, requestCode, bundle)
            } catch (e: Exception) {
                Logger.e(TAG, e)
            }
        }

        fun setFullScreen(activity: Activity) {
            try {
                activity.requestWindowFeature(Window.FEATURE_NO_TITLE)
                activity.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
            } catch (e: Exception) {
                Logger.e(TAG, e)
            }
        }

        fun setLandscape(activity: Activity) {
            try {
                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            } catch (e: Exception) {
                Logger.e(TAG, e)
            }
        }

        fun setPortrait(activity: Activity) {
            try {
                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            } catch (e: Exception) {
                Logger.e(TAG, e)
            }
        }

        fun getScreenRotation(activity: Activity): Int = when (activity.windowManager.defaultDisplay.rotation) {
            Surface.ROTATION_0 -> 0
            Surface.ROTATION_90 -> 90
            Surface.ROTATION_180 -> 180
            Surface.ROTATION_270 -> 279
            else -> 0
        }

        fun isLockScreen(context: Context): Boolean {
            try {
                val keyguardManager: KeyguardManager = context.applicationContext.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
                return keyguardManager.isDeviceLocked
            } catch (e: Exception) {
                Logger.e(TAG, e)
            }

            return false
        }

        fun setSleepDuration(context: Context, duration: Int) {
            try {
                Settings.System.putInt(context.applicationContext.contentResolver, Settings.System.SCREEN_OFF_TIMEOUT, duration)
            } catch (e: Exception) {
                Logger.e(TAG, e)
            }
        }

        fun getSleepDuration(context: Context): Int {
            try {
                Settings.System.getInt(context.applicationContext.contentResolver, Settings.System.SCREEN_OFF_TIMEOUT)
            } catch (e: Exception) {
                Logger.e(TAG, e)
            }

            return 0
        }
    }
}