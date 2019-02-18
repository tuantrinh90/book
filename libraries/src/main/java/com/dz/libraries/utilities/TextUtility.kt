package com.dz.libraries.utilities

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Paint
import android.text.SpannableString
import android.text.style.URLSpan
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import com.dz.libraries.loggers.Logger
import com.dz.libraries.views.textviews.URLSpanNoUnderline

class TextUtility {
    companion object {
        private const val TAG = "TextUtility"

        fun setSelection(textView: TextView) {
            try {
                textView.post {
                    val text = textView.text?.toString()
                    if (textView is EditText && !StringUtility.isNullOrEmpty(text)) {
                        textView.setSelection(text!!.length)
                    }
                }
            } catch (e: Exception) {
                Logger.e(TAG, e)
            }
        }

        fun setFocus(context: Context, textView: TextView) {
            try {
                textView.requestFocus()

                val inputMethodManager: InputMethodManager? = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager?.showSoftInput(textView, InputMethodManager.SHOW_IMPLICIT)

                setSelection(textView)
            } catch (e: Exception) {
                Logger.e(TAG, e)
            }
        }

        fun setFocusDialog(activity: Activity, dialog: Dialog, textView: TextView) {
            textView.requestFocus()

            dialog.setOnShowListener {
                val inputMethodManager: InputMethodManager? = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager?.showSoftInput(textView, InputMethodManager.SHOW_IMPLICIT)

                setSelection(textView)
            }
        }

        /**
         * remove underline link & change color text
         *
         * @param textView
         */
        fun stripUnderlines(textView: TextView, linkColor: Int) {
            val spannable = SpannableString(textView.text)
            val spans = spannable.getSpans(0, spannable.length, URLSpan::class.java)
            for (span: URLSpan in spans) {
                val start = spannable.getSpanStart(span)
                val end = spannable.getSpanEnd(span)
                spannable.removeSpan(span)
                spannable.setSpan(URLSpanNoUnderline(span.url, linkColor), start, end, 0)
            }
            textView.text = spannable
        }

        fun underLine(textView: TextView){
            textView.paintFlags = textView.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        }
    }
}