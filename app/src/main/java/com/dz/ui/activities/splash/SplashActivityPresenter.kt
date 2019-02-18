package com.dz.ui.activities.splash

import com.dz.commons.presenters.BaseActivityMvpPresenter
import com.dz.di.AppComponent

class SplashActivityPresenter(appComponent: AppComponent) : BaseActivityMvpPresenter<ISplashActivityView>(appComponent), ISplashActivityPresenter