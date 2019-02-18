package com.dz.ui.fragments.accounts.myprofiles

import com.dz.commons.presenters.BaseFragmentMvpPresenter
import com.dz.di.AppComponent

class MyProfilePresenter(appComponent: AppComponent) : BaseFragmentMvpPresenter<IMyProfileView>(appComponent), IMyProfilePresenter