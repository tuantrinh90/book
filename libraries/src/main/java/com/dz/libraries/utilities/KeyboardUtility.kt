package com.dz.libraries.utilities

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.dz.libraries.loggers.Logger

class KeyboardUtility {
    companion object {
        private const val TAG = "KeyboardUtility"

        fun dontShowKeyboard(activity: Activity) {
            try {
                activity.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
                hideSoftKeyboard(activity)
            } catch (e: Exception) {
                Logger.e(TAG, e)
            }
        }

        fun hideSoftKeyboard(activity: Activity) {
            try {
                val inputMethodManager: InputMethodManager? = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager?.hideSoftInputFromWindow(activity.currentFocus?.windowToken, 0)
            } catch (e: Exception) {
                Logger.e(TAG, e)
            }
        }

        fun hideSoftKeyboard(activity: Activity, editText: EditText) {
            try {
                val inputMethodManager: InputMethodManager? = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager?.hideSoftInputFromWindow(editText.windowToken, 0)
            } catch (e: Exception) {
                Logger.e(TAG, e)
            }
        }

        fun hideSoftKeyboard(activity: Activity, view: View) {
            try {
                if (view !is EditText) {
                    view.setOnTouchListener { _, _ ->
                        hideSoftKeyboard(activity)
                        false
                    }
                }

                if (view is ViewGroup) {
                    for (i in 0 until view.childCount) {
                        hideSoftKeyboard(activity, view.getChildAt(i))
                    }
                }
            } catch (e: Exception) {
                Logger.e(TAG, e)
            }
        }
    }
}


