package com.dz.commons.activities.alonefragment

import com.dz.commons.presenters.BaseActivityMvpPresenter
import com.dz.di.AppComponent

class AloneFragmentActivityPresenter(appComponent: AppComponent) : BaseActivityMvpPresenter<IAloneFragmentActivityView>(appComponent), IAloneFragmentActivityPresenter