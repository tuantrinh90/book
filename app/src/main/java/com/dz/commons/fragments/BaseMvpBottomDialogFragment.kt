package com.dz.commons.fragments

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.dz.commons.presenters.IBaseFragmentMvpPresenter
import com.dz.commons.views.IBaseFragmentMvpView
import com.dz.ui.R

abstract class BaseMvpBottomDialogFragment<V : IBaseFragmentMvpView, P : IBaseFragmentMvpPresenter<V>> : BaseMvpDialogFragment<V, P>() {
    companion object {
        private const val TAG = "BaseMvpBottomDialogFragment"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.apply {
            setGravity(Gravity.BOTTOM)
            setWindowAnimations(R.style.DialogAnimationBottomToTop)
            requestFeature(Window.FEATURE_NO_TITLE)
        };
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog?.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setLayout(WindowManager.LayoutParams.MATCH_PARENT, attributes?.height ?: 0)
        }
    }
}