package com.dz.libraries.utilities

import android.app.Activity
import android.os.Handler
import android.view.Gravity
import com.dz.libraries.loggers.Logger

class BackUtility {
    companion object {
        private const val TAG = "BackUtility"

        // check status
        private var isDoubleClick = false

        /**
         * use to confirm exit app when user double click to back action
         * @param activity
         * @param messageConfirm
         */
        fun onClickExit(activity: Activity, messageConfirm: String) {
            try {
                // double tap to exit app
                if (isDoubleClick) {
                    System.exit(0)
                    return
                }

                isDoubleClick = true
                ToastUtility.show(activity, messageConfirm, Gravity.BOTTOM)
                Handler().postDelayed({ isDoubleClick = false }, 2 * 2000)
            } catch (e: Exception) {
                Logger.e(TAG, e)
            }
        }
    }
}