package com.dz.libraries.utilities

import android.app.Activity
import android.app.ActivityManager
import android.app.Application
import android.content.ComponentCallbacks2
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.util.Log
import com.dz.libraries.loggers.Logger
import java.util.*

@Suppress("DEPRECATION")
class ApplicationUtility {
    companion object {
        private const val TAG = "ApplicationUtility"

        // list activity in stack
        private var activities: MutableList<Activity> = LinkedList()

        /**
         * activity lifecycle
         */
        private var mCallbacks = ApplicationLifecycleHandler()

        // foreground
        var isForeground = false

        // background
        var isBackground = false

        /**
         * @param mApplication
         */
        fun init(mApplication: Application) {
            mApplication.registerActivityLifecycleCallbacks(mCallbacks)
            mApplication.registerComponentCallbacks(mCallbacks)
        }

        /**
         * get version code of app
         *
         * @param context
         * @return version code of app
         */
        fun getVerCode(context: Context): Int {
            var verCode = -1

            try {
                verCode = context.packageManager.getPackageInfo(context.packageName, 0).versionCode
            } catch (e: PackageManager.NameNotFoundException) {
                Logger.e(TAG, e)
            }

            return verCode
        }

        /**
         * get version name of app
         *
         * @param context
         * @return version name of app
         */
        fun getVerName(context: Context): String {
            var verName = ""

            try {
                verName = context.packageManager.getPackageInfo(context.packageName, 0).versionName
            } catch (e: PackageManager.NameNotFoundException) {
                Logger.e(TAG, e)
            }

            return verName
        }

        /**
         * get to play tore to rating...
         *
         * @param activity
         */
        fun goPlayStoreApp(activity: Activity) {
            val appPackageName = activity.packageName // getPackageName() from Context or Activity object
            try {
                activity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName")))
            } catch (ex: android.content.ActivityNotFoundException) {
                activity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")))
            }
        }
    }

    internal class ApplicationLifecycleHandler : Application.ActivityLifecycleCallbacks, ComponentCallbacks2 {
        var numStarted = 0

        override fun onActivityCreated(activity: Activity, bundle: Bundle?) {
            activities.add(activity)
        }

        override fun onActivityStarted(activity: Activity) {
            // app went to foreground when numStarted == 0
            isForeground = numStarted == 0
            numStarted++
        }

        override fun onActivityResumed(activity: Activity) {

        }

        override fun onActivityPaused(activity: Activity) {

        }

        override fun onActivityStopped(activity: Activity) {
            numStarted--
            // app went to background numStarted == 0
            isBackground = numStarted == 0
        }

        override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle?) {

        }

        override fun onActivityDestroyed(activity: Activity) {
            activities.remove(activity)
        }

        /**
         * Called when the operating system has determined that it is a good
         * time for a process to trim unneeded memory from its process.  This will
         * happen for example when it goes in the background and there is not enough
         * memory to keep as many background processes running as desired.  You
         * should never compare to exact values of the level, since new intermediate
         * values may be added -- you will typically want to compare if the value
         * is greater or equal to a level you are interested in.
         *
         *
         * To retrieve the processes current trim level at any point, you can
         * use [ ActivityManager.getMyMemoryState(RunningAppProcessInfo)][ActivityManager.getMyMemoryState].
         *
         * @param level The context of the trim, giving a hint of the amount of
         * trimming the application may like to perform.
         */
        override fun onTrimMemory(level: Int) {
            if (level == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
                Log.d(TAG, "App went to background")
            }

            if (level == ComponentCallbacks2.TRIM_MEMORY_COMPLETE) {
            }
        }

        /**
         * Called by the system when the device configuration changes while your
         * component is running.  Note that, unlike activities, other components
         * are never restarted when a configuration changes: they must always deal
         * with the results of the change, such as by re-retrieving resources.
         *
         *
         * At the time that this function has been called, your Resources
         * object will have been updated to return resource values matching the
         * new configuration.
         *
         *
         * For more information, read [Handling Runtime Changes]({@docRoot}guide/topics/resources/runtime-changes.html).
         *
         * @param newConfig The new device configuration.
         */
        override fun onConfigurationChanged(newConfig: Configuration) {

        }

        /**
         * This is called when the overall system is running low on memory, and
         * actively running processes should trim their memory usage.  While
         * the exact point at which this will be called is not defined, generally
         * it will happen when all background process have been killed.
         * That is, before reaching the point of killing processes hosting
         * service and foreground UI that we would like to avoid killing.
         *
         *
         * You should implement this method to release
         * any caches or other unnecessary resources you may be holding on to.
         * The system will perform a garbage collection for you after returning from this method.
         *
         * Preferably, you should implement [ComponentCallbacks2.onTrimMemory] from
         * [ComponentCallbacks2] to incrementally unload your resources based on various
         * levels of memory demands.  That API is available for API level 14 and higher, so you should
         * only use this [.onLowMemory] method as a fallback for older versions, which can be
         * treated the same as [ComponentCallbacks2.onTrimMemory] with the [ ][ComponentCallbacks2.TRIM_MEMORY_COMPLETE] level.
         */
        override fun onLowMemory() {

        }
    }
}