package com.dz.commons.fragments.toast

import com.dz.commons.presenters.BaseFragmentMvpPresenter
import com.dz.di.AppComponent

class ToastPresenter(appComponent: AppComponent) : BaseFragmentMvpPresenter<IToastView>(appComponent), IToastPresenter {
}