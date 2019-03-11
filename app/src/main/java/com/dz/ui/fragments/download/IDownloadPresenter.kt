package com.dz.ui.fragments.history

import com.dz.commons.presenters.IBaseFragmentMvpPresenter

interface IDownloadPresenter : IBaseFragmentMvpPresenter<IDownloadView> {
    fun getBook()
}