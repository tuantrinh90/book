package com.dz.ui.fragments

import android.content.Context
import com.dz.commons.fragments.BaseMvpFragment
import com.dz.commons.presenters.IBaseFragmentMvpPresenter
import com.dz.commons.views.IBaseFragmentMvpView
import com.dz.ui.activities.main.MainActivity

abstract class BaseMainFragment<V : IBaseFragmentMvpView, P : IBaseFragmentMvpPresenter<V>> : BaseMvpFragment<V, P>() {
    lateinit var mMainActivity: MainActivity

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is MainActivity) {
            mMainActivity = context
        }
    }
}