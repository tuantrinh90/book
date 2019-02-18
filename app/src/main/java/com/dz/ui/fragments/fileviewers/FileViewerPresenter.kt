package com.dz.ui.fragments.fileviewers

import com.dz.commons.presenters.BaseFragmentMvpPresenter
import com.dz.di.AppComponent

class FileViewerPresenter(appComponent: AppComponent) : BaseFragmentMvpPresenter<IFileViewerView>(appComponent), IFileViewerPresenter {
}