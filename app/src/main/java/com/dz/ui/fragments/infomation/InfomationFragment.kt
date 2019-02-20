package com.dz.ui.fragments.history

import android.os.Bundle
import android.view.View
import com.dz.ui.R
import com.dz.ui.fragments.BaseMainFragment
import io.reactivex.disposables.CompositeDisposable

class InfomationFragment : BaseMainFragment<IDownloadView, IDownloadPresenter>(), IDownloadView {
    enum class Type {
        OPEN, CLOSE
    }

    override fun createPresenter(): IDownloadPresenter = DownloadPresenter(appComponent)

    override val resourceId: Int get() = R.layout.history_fragment

    override val titleId: Int get() = R.string.home

    var mType = InfomationFragment.Type.OPEN

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindButterKnife(view)
    }

    fun setCompositeDisposable(compositeDisposable: CompositeDisposable): InfomationFragment {
        return this
    }

    fun setType(type: InfomationFragment.Type): InfomationFragment {
        mType = type
        return this
    }

}