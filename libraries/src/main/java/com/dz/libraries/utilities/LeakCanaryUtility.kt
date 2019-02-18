package com.dz.libraries.utilities

import android.app.Application
import com.squareup.leakcanary.LeakCanary

class LeakCanaryUtility {
    companion object {
        fun init(application: Application){
            if (LeakCanary.isInAnalyzerProcess(application)){
                // This process is dedicated to LeakCanary for heap analysis. You should not init your app in this process.
                return
            }

            LeakCanary.install(application)
        }
    }
}