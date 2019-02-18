package com.dz.libraries.utilities

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.dz.libraries.R

class NotificationUtility private constructor(application: Application) {
    companion object {
        private const val TAG = "NotificationUtility"

        // instance
        private var notificationUtility: NotificationUtility? = null

        /**
         * instance
         *
         * @param application
         * @return
         */
        fun init(application: Application) {
            notificationUtility = NotificationUtility(application)
        }

        /**
         * instance
         *
         * @return
         */
        fun instance(): NotificationUtility {
            if (notificationUtility == null) throw NullPointerException()
            return notificationUtility!!
        }
    }

    // variable
    private var mApplication: Application = application
    private var notificationManager: NotificationManager? = null

    init {
        notificationManager = application.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            val channel = NotificationChannel(application.packageName, application.packageName, NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = application.packageName
            notificationManager?.createNotificationChannel(channel)
        }
    }

    fun getPendingIntent(clazz: Class<*>, bundle: Bundle? = null, requestCode: Int = 0): PendingIntent {
        val intent = Intent(mApplication, clazz)
        bundle?.let { intent.putExtras(bundle) }
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        return PendingIntent.getActivity(mApplication, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    /***
     * @param pendingIntent
     * @param title          , title of the notification
     * @param smallIconResId , the right icon visible
     * @param largeIconResId , the left icon visible
     * @param message        , message displayed to the user
     * @param summaryText    ,
     * @param autoCancel     , true to cancel on click
     * @return a notification to show
     */
    fun getNotification(pendingIntent: PendingIntent,
                        title: String? = "", message: String? = "",
                        summaryText: String? = "",
                        smallIconResId: Int = R.mipmap.ic_launcher,
                        largeIconResId: Int = R.mipmap.ic_launcher,
                        autoCancel: Boolean = false): Notification? {
        val bigText = NotificationCompat.BigTextStyle()

        // message
        StringUtility.with(message).doIfPresent { bigText.bigText(it) }

        // title
        StringUtility.with(title).doIfPresent { bigText.setBigContentTitle(it) }

        // summary text
        StringUtility.with(summaryText).doIfPresent { bigText.setSummaryText(it) }

        // big icon
        val bigIcon = BitmapFactory.decodeResource(mApplication.resources, largeIconResId)

        // builder
        val builder = NotificationCompat.Builder(mApplication, mApplication.packageName)
                .setContentIntent(pendingIntent)
                .setStyle(bigText)
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(smallIconResId)
                .setLargeIcon(bigIcon)
                .setAutoCancel(autoCancel)

        // update title
        StringUtility.with(title).doIfPresent {
            builder.apply {
                setContentTitle(it)
                setTicker(it)
            }
        }

        // update message
        StringUtility.with(message).doIfPresent {
            builder.apply {
                setContentText(it)
            }
        }

        // color
        builder.color = ContextCompat.getColor(mApplication, R.color.colorPrimary)

        // build
        return builder.build()
    }

    /***
     * Show a notification on the device
     *
     * @param id
     * @param notification
     */
    fun show(id: Int, notification: Notification?) = notificationManager?.notify(id, notification)

    /**
     * clear notification
     */
    fun clearNotification() = notificationManager?.cancelAll()
}