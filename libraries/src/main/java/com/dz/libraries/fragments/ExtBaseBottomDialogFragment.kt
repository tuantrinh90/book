package com.dz.libraries.fragments

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.dz.libraries.R
import com.dz.libraries.loggers.Logger

abstract class ExtBaseBottomDialogFragment : DialogFragment() {
    companion object {
        private const val TAG = "ExtBaseBottomDialogFragment"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            dialog?.window?.apply {
                setGravity(Gravity.BOTTOM)
                setWindowAnimations(R.style.DialogAnimationBottomToTop)
                requestFeature(Window.FEATURE_NO_TITLE)
            }
        } catch (e: Exception) {
            Logger.e(TAG, e)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        try {
            dialog?.window?.apply {
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                setLayout(WindowManager.LayoutParams.MATCH_PARENT, attributes?.height ?: 0)
            }
        } catch (e: Exception) {
            Logger.e(TAG, e)
        }
    }
}
