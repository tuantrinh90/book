package com.dz.ui.fragments.homes

import com.dz.commons.presenters.BaseFragmentMvpPresenter
import com.dz.di.AppComponent

class HomePresenter(appComponent: AppComponent) : BaseFragmentMvpPresenter<IHomeView>(appComponent), IHomePresenter {
}