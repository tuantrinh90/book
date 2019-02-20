package com.dz.ui.fragments.history

import com.dz.commons.presenters.BaseFragmentMvpPresenter
import com.dz.di.AppComponent

class HistoryPresenter(appComponent: AppComponent) : BaseFragmentMvpPresenter<IDownloadView>(appComponent), IDownloadPresenter {
}