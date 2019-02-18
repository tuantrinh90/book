package com.dz.libraries.utilities

import android.content.Context
import android.graphics.Paint
import android.widget.TextView
import com.dz.libraries.loggers.Logger

class FontUtility {
    companion object {
        private const val TAG = "FontUtility"

        fun setCustomizeTypeFace(context: Context, textView: TextView, fontPath: String) {
            try {
                val font = TypefaceUtility.with(context).get(fontPath)
                OptionalUtility.with(font).doIfPresent {
                    textView.typeface = it
                    textView.paintFlags = textView.paintFlags or Paint.SUBPIXEL_TEXT_FLAG
                }
            } catch (e: Exception) {
                Logger.e(TAG, e)
            }
        }
    }
}