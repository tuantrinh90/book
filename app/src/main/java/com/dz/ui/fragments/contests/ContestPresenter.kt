package com.dz.ui.fragments.contests

import com.dz.commons.presenters.BaseFragmentMvpPresenter
import com.dz.di.AppComponent

class ContestPresenter(appComponent: AppComponent) : BaseFragmentMvpPresenter<IContestView>(appComponent), IContestPresenter