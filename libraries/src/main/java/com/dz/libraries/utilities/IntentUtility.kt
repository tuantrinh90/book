package com.dz.libraries.utilities

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import java.io.File

class IntentUtility {
    companion object {
        fun getInstallAppIntent(context: Context, file: File, authority: String): Intent? {
            if (OptionalUtility.isNullOrEmpty(file)) return null

            val intent = Intent(Intent.ACTION_VIEW)

            val uri: Uri?
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                uri = Uri.fromFile(file)
            } else {
                intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                uri = FileProvider.getUriForFile(context, authority, file)
            }

            intent.setDataAndType(uri, "application/vnd.android.package-archive")
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            return intent
        }

        fun getUninstallAppIntent(packageName: String): Intent {
            val intent = Intent(Intent.ACTION_DELETE)
            intent.data = Uri.parse("package:$packageName")
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            return intent
        }

        fun getLauncherAppIntent(context: Context, packageName: String): Intent? = context.packageManager.getLaunchIntentForPackage(packageName)

        fun getAppDetailSettingIntent(packageName: String): Intent {
            val intent = Intent("android.settings.APPLICATION_DETAILS_SETTINGS")
            intent.data = Uri.parse("package:$packageName")
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            return intent
        }

        fun getShareTextContentIntent(content: String): Intent {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, content)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            return intent
        }

        fun getShareImageContentIntent(content: String, uri: Uri): Intent {
            val intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_TEXT, content)
            intent.putExtra(Intent.EXTRA_STREAM, uri)
            intent.type = "image/*"
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            return intent
        }

        fun getShutdownIntent(): Intent {
            val intent = Intent(Intent.ACTION_SHUTDOWN)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            return intent
        }

        fun getDialIntent(phoneNumber: String): Intent {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            return intent
        }

        fun getCallIntent(phoneNumber: String): Intent {
            val intent = Intent("android.intent.action.CALL", Uri.parse("tel:$phoneNumber"))
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            return intent
        }

        fun getSendSmsIntent(phoneNumber: String, content: String): Intent {
            val uri = Uri.parse("smsto:$phoneNumber")
            val intent = Intent(Intent.ACTION_SENDTO, uri)
            intent.putExtra("sms_body", content)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            return intent
        }
    }
}