package com.dz.commons.presenters

import android.os.Bundle
import com.dz.commons.views.IBaseMvpView

interface IBaseMvpPresenter<V : IBaseMvpView> {
    fun attachView(view: V)

    fun getView(consumer: (v: V) -> Unit)

    fun isViewAttached(): Boolean

    fun detachView()

    fun processArguments(arguments: Bundle?)

    fun saveInstanceState(bundle: Bundle?)

    fun restoreInstanceState(bundle: Bundle?)
}