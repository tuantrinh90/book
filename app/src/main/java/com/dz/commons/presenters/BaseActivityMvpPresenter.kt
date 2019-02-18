package com.dz.commons.presenters

import com.dz.commons.views.IBaseActivityMvpView
import com.dz.di.AppComponent

abstract class BaseActivityMvpPresenter<V : IBaseActivityMvpView>(appComponent: AppComponent) : BaseMvpPresenter<V>(appComponent), IBaseActivityMvpPresenter<V>