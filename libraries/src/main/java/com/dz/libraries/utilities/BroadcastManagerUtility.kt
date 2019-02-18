package com.dz.libraries.utilities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.localbroadcastmanager.content.LocalBroadcastManager

class BroadcastManagerUtility {
    companion object {
        /**
         * register broadcast receiver
         *
         * @param context
         * @param broadcastReceiver
         * @param intentFilter
         */
        fun registerReceiver(context: Context, broadcastReceiver: BroadcastReceiver, intentFilter: IntentFilter) {
            LocalBroadcastManager.getInstance(context).registerReceiver(broadcastReceiver, intentFilter)
        }

        /**
         * unregister broadcast receiver
         *
         * @param context
         * @param broadcastReceiver
         */
        fun unregisterReceiver(context: Context, broadcastReceiver: BroadcastReceiver) {
            LocalBroadcastManager.getInstance(context).unregisterReceiver(broadcastReceiver)
        }

        /**
         * send broad cast in app
         *
         * @param context
         * @param intent
         */
        fun sendBroadcast(context: Context, intent: Intent) {
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
        }
    }
}