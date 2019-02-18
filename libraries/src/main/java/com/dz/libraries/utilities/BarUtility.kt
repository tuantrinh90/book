package com.dz.libraries.utilities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.util.TypedValue
import android.view.*
import androidx.annotation.ColorRes
import androidx.annotation.RequiresApi
import com.dz.libraries.loggers.Logger

@Suppress("DEPRECATION")
class BarUtility {
    companion object {
        private const val TAG = "BarUtility"

        /**
         * @return height of status bar
         */
        fun getStatusBarHeight(context: Context): Int {
            try {
                val resources = context.resources
                val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
                return resources.getDimensionPixelSize(resourceId)
            } catch (e: Exception) {
                Logger.e(TAG, e)
            }

            return 0
        }

        /**
         * @param activity
         * @return
         */
        fun getActionBarHeight(activity: Activity): Int {
            try {
                val typedValue = TypedValue()
                if (activity.theme.resolveAttribute(android.R.attr.actionBarSize, typedValue, true)) {
                    return TypedValue.complexToDimensionPixelSize(typedValue.data, activity.resources.displayMetrics)
                }
            } catch (e: Exception) {
                Logger.e(TAG, e)
            }

            return 0
        }

        /**
         * @return height of navigation bar
         */
        fun getNavBarHeight(context: Context): Int {
            try {
                val hasMenuKey = ViewConfiguration.get(context).hasPermanentMenuKey()
                val hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK)

                if (!hasMenuKey && !hasBackKey) {
                    val res = context.resources
                    val resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android")
                    return res.getDimensionPixelSize(resourceId)
                }
            } catch (e: Resources.NotFoundException) {
                Logger.e(TAG, e)
            }

            return 0
        }

        /**
         * hide navigation bar
         * @param activity
         */
        @SuppressLint("ObsoleteSdkInt")
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        fun hideNavBar(activity: Activity) {
            try {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) return

                if (getNavBarHeight(activity) > 0) {
                    val decorView = activity.window.decorView
                    val uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    decorView.systemUiVisibility = uiOptions
                }
            } catch (e: Exception) {
                Logger.e(TAG, e)
            }
        }

        /**
         * @param activity
         * @param colorPrimaryDark
         */
        fun setTranslucentForDrawer(activity: Activity, @ColorRes colorPrimaryDark: Int) {
            try {
                val window = activity.window

                // clear FLAG_TRANSLUCENT_STATUS flag:
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

                // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

                // finally change the color again to dark
                window.statusBarColor = activity.resources.getColor(colorPrimaryDark)
            } catch (e: Exception) {
                Logger.e(TAG, e)
            }
        }
    }
}