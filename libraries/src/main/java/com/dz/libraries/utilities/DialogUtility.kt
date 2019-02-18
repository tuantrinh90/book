package com.dz.libraries.utilities

import android.content.Context
import android.content.DialogInterface
import android.view.View
import androidx.appcompat.app.AlertDialog

class DialogUtility {
    companion object {
        var alertDialog: AlertDialog? = null

        fun messageBox(context: Context, title: String?,
                       message: String?, ok: String?) {
            dismiss()
            val builder = AlertDialog.Builder(context)
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton(ok) { dialog, _ -> dialog.dismiss() }
                    .setCancelable(false)
            alertDialog = builder.show()
        }

        /**
         * show message
         * @param context
         * @param title
         * @param message
         * @param ok
         * @param callback
         */
        fun messageBox(context: Context, title: String?,
                       message: String?, ok: String?,
                       callback: DialogInterface.OnClickListener) {
            dismiss()
            val builder = AlertDialog.Builder(context)
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton(ok, callback)
                    .setCancelable(false)
            alertDialog = builder.show()
        }

        /**
         * show message
         * @param context
         * @param title
         * @param message
         * @param ok
         * @param cancel
         * @param yesCallback
         * @param cancelCallback
         * @return
         */
        fun messageBox(context: Context, title: String?,
                       message: String?, ok: String?, cancel: String?,
                       yesCallback: DialogInterface.OnClickListener,
                       cancelCallback: DialogInterface.OnClickListener) {
            dismiss()
            val builder = AlertDialog.Builder(context)
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton(ok, yesCallback)
                    .setNegativeButton(cancel, cancelCallback)
                    .setCancelable(false)
            alertDialog = builder.show()
        }

        /**
         * show message
         * @param context
         * @param view
         * @return a alert dialog to dismiss or implement some action invoke it
         */
        fun customBox(context: Context, view: View) {
            dismiss()
            val builder = AlertDialog.Builder(context)
                    .setView(view)
                    .setCancelable(true)
            alertDialog = builder.show()
        }


        /**
         * show confirm message
         * @param context
         * @param title
         * @param message
         * @param yes
         * @param no
         * @param yesCallback
         */
        fun confirmBox(context: Context, title: String?,
                       message: String?, yes: String?,
                       no: String?,
                       yesCallback: DialogInterface.OnClickListener) {
            dismiss()
            val builder = AlertDialog.Builder(context)
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton(yes, yesCallback)
                    .setNegativeButton(no) { dialog, _ -> dialog.dismiss() }
                    .setCancelable(false)
            alertDialog = builder.show()
        }


        /**
         * show confirm message
         * @param context
         * @param title
         * @param message
         * @param yes
         * @param no
         * @param yesCallback
         * @param cancelCallback
         */
        fun confirmBox(context: Context, title: String?,
                       message: String?, yes: String?,
                       no: String?,
                       yesCallback: DialogInterface.OnClickListener,
                       cancelCallback: DialogInterface.OnClickListener) {
            dismiss()
            val builder = AlertDialog.Builder(context)
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton(yes, yesCallback)
                    .setNegativeButton(no, cancelCallback)
                    .setCancelable(false)
            alertDialog = builder.show()
        }

        fun dismiss() {
            alertDialog?.dismiss()
            alertDialog = null
        }
    }
}