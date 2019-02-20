package com.dz.ui.fragments.history

import android.os.Bundle
import android.view.View
import com.dz.ui.R
import com.dz.ui.fragments.BaseMainFragment
import io.reactivex.disposables.CompositeDisposable

class DownloadFragment : BaseMainFragment<IDownloadView, IDownloadPresenter>(), IDownloadView {

    override fun createPresenter(): IDownloadPresenter = DownloadPresenter(appComponent)

    override val resourceId: Int get() = R.layout.history_fragment

    override val titleId: Int get() = R.string.home

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindButterKnife(view)
    }
}