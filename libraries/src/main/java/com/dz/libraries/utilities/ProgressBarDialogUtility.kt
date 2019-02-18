@file:Suppress("DEPRECATION")

package com.dz.libraries.utilities

import android.app.ProgressDialog
import android.content.Context
import com.dz.libraries.R

@Suppress("DEPRECATION")
class ProgressBarDialogUtility {
    companion object {
        private var progressDialog: ProgressDialog? = null
        private var progressBarStyle = ProgressDialog.STYLE_SPINNER
        private var message: String? = null

        fun setProgressBarStyle(pbs: Int) {
            progressBarStyle = pbs
        }

        fun setMessage(msg: String?) {
            message = msg
        }

        fun setPercentProgressBar(percent: Int) {
            progressDialog?.progress = percent
        }

        fun show(context: Context) {
            dismiss()

            // show
            progressDialog = ProgressDialog(context, R.style.StyleProgressDialog)
            progressDialog?.apply {
                StringUtility.with(message).doIfPresent { setMessage(it) }
                setCancelable(false)
                setCanceledOnTouchOutside(false)
                setProgressStyle(progressBarStyle)
                progress = 0
                show()
            }
        }

        fun dismiss() {
            progressDialog?.dismiss()
            progressDialog= null
            progressBarStyle = ProgressDialog.STYLE_SPINNER
        }
    }
}