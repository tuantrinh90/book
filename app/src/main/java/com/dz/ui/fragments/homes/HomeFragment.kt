package com.dz.ui.fragments.homes

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.dz.ui.R
import com.dz.ui.fragments.BaseMainFragment
import io.reactivex.disposables.CompositeDisposable

class HomeFragment : BaseMainFragment<IHomeView, IHomePresenter>(), IHomeView {
    enum class Type {
        OPEN, CLOSE
    }

    override fun createPresenter(): IHomePresenter = HomePresenter(appComponent)

    override val resourceId: Int get() = R.layout.home_fragment

    override val titleId: Int get() = R.string.home

    var mType = HomeFragment.Type.OPEN

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindButterKnife(view)
    }

    fun setCompositeDisposable(compositeDisposable: CompositeDisposable): HomeFragment {
        return this
    }

    fun setType(type: HomeFragment.Type): HomeFragment {
        mType = type
        return this
    }

}