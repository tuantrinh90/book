package com.dz.libraries.utilities

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.Toast

class ToastUtility {
    companion object {
        /**
         * show message in app
         * @param context
         * @param message
         */
        fun show(context: Context, message: String?) {
            if (StringUtility.isNullOrEmpty(message)) return
            val toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
        }

        /**
         * show message in app
         * @param context
         * @param message
         * @param gravity
         */
        fun show(context: Context, message: String?, gravity: Int) {
            if (StringUtility.isNullOrEmpty(message)) return
            val toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
            toast.setGravity(gravity, 0, 0)
            toast.show()
        }

        /**
         * show message in app
         * @param context
         * @param message
         * @param gravity
         * @param xOffset
         * @param yOffset
         */
        fun show(context: Context, message: String?, gravity: Int,
                 xOffset: Int, yOffset: Int) {
            if (StringUtility.isNullOrEmpty(message)) return
            val toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
            toast.setGravity(gravity, xOffset, yOffset)
            toast.show()
        }

        /**
         * show message in app
         * @param context
         * @param view
         */
        fun show(context: Context, view: View) {
            val toast = Toast(context)
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.duration = Toast.LENGTH_SHORT
            toast.view = view
            toast.show()
        }

        /**
         * show message in app
         * @param context
         * @param view
         * @param gravity
         */
        fun show(context: Context, view: View, gravity: Int) {
            val toast = Toast(context)
            toast.setGravity(gravity, 0, 0)
            toast.duration = Toast.LENGTH_SHORT
            toast.view = view
            toast.show()
        }
    }
}