package com.dz.ui.activities.main

import com.dz.commons.presenters.BaseActivityMvpPresenter
import com.dz.di.AppComponent

class MainActivityPresenter(appComponent: AppComponent) : BaseActivityMvpPresenter<IMainActivityView>(appComponent), IMainActivityPresenter {

}