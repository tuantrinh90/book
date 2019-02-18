package com.dz.commons.presenters

import com.dz.commons.views.IBaseFragmentMvpView
import java.io.File

interface IBaseFragmentMvpPresenter<V : IBaseFragmentMvpView> : IBaseMvpPresenter<V> {
    fun uploadFile(file: File)
}