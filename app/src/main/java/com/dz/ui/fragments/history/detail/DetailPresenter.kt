package com.dz.ui.fragments.history

import com.dz.commons.presenters.BaseFragmentMvpPresenter
import com.dz.di.AppComponent

class DetailPresenter(appComponent: AppComponent) : BaseFragmentMvpPresenter<IDetailView>(appComponent), IDetailPresenter {
}