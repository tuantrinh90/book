package com.dz.libraries.utilities

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.os.Vibrator

@Suppress("DEPRECATION")
class VibratorUtility private constructor(ctx: Context) {
    companion object {
        private const val TAG = "VibratorUtility"
        private const val VIBRATOR_TIME: Long = 2 * 1000

        private var vibratorUtility: VibratorUtility? = null

        fun with(context: Context): VibratorUtility {
            if (vibratorUtility == null) {
                vibratorUtility = VibratorUtility(context)
            }

            return vibratorUtility!!
        }
    }

    private val vibrator: Vibrator = ctx.applicationContext.getSystemService(Service.VIBRATOR_SERVICE) as Vibrator

    fun vibrate() = vibrate(VIBRATOR_TIME)

    @SuppressLint("MissingPermission")
    fun vibrate(milliseconds: Long) = vibrator.vibrate(milliseconds)

    @SuppressLint("MissingPermission")
    fun cancel() = vibrator.cancel()
}