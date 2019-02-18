package com.dz.libraries.applications

import androidx.multidex.MultiDexApplication
import com.dz.libraries.loggers.Logger
import com.dz.libraries.preferences.AppPreferences
import com.dz.libraries.utilities.*
import java.io.File

open class ExtApplication : MultiDexApplication() {
    companion object {
        // folder store log, image of application
        private var pathRootProject: File? = null

        // instance of application
        lateinit var instance: ExtApplication

        /**
         * init application
         */
        fun init(application: ExtApplication) {
            instance = application
        }

        /**
         * * @return path root project
         */
        fun getPathProject(): File? {
            OptionalUtility.with(pathRootProject)
                    .doIfEmpty { pathRootProject = FileUtility.getRootPath(instance, instance.packageName) }
            return pathRootProject
        }
    }

    override fun onCreate() {
        super.onCreate()

        // set instance application
        ExtApplication.init(this)

        // ext utils
        ApplicationUtility.init(this)

        // init instance of app preference
        AppPreferences.init(this)

        // init leak canary
        LeakCanaryUtility.init(this)

        // init notification
        NotificationUtility.init(this)

        // setup path project for logging, image path....
        getPathProject()

        // setup Logger
        Logger.setEnableLog(true)

        // update path to log error
        OptionalUtility.with(getPathProject())
                .doIfPresent { Logger.setPathSaveLog(it.absolutePath, packageName, "log") }
    }
}