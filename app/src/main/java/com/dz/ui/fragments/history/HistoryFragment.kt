package com.dz.ui.fragments.history

import android.os.Bundle
import android.view.View
import com.dz.ui.R
import com.dz.ui.fragments.BaseMainFragment
import io.reactivex.disposables.CompositeDisposable

class HistoryFragment : BaseMainFragment<IDownloadView, IDownloadPresenter>(), IDownloadView {
    enum class Type {
        OPEN, CLOSE
    }

    override fun createPresenter(): IDownloadPresenter = DownloadPresenter(appComponent)

    override val resourceId: Int get() = R.layout.home_fragment

    override val titleId: Int get() = R.string.home

    var mType = HistoryFragment.Type.OPEN

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindButterKnife(view)
    }

    fun setCompositeDisposable(compositeDisposable: CompositeDisposable): HistoryFragment {
        return this
    }

    fun setType(type: HistoryFragment.Type): HistoryFragment {
        mType = type
        return this
    }

}