package com.dz.ui.fragments.history

import android.os.Bundle
import android.view.View
import com.dz.models.database.Book
import com.dz.ui.R
import com.dz.ui.fragments.BaseMainFragment

class InfomationFragment : BaseMainFragment<IDownloadView, IDownloadPresenter>(), IDownloadView {
    override fun setData(response: List<Book?>?) {
    }

    override fun createPresenter(): IDownloadPresenter = DownloadPresenter(appComponent)

    override val resourceId: Int get() = R.layout.infomation_fragment

    override val titleId: Int get() = R.string.home

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindButterKnife(view)
    }

}