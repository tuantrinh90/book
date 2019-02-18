package com.dz.ui.fragments.accounts.signins

import com.dz.commons.presenters.IBaseFragmentMvpPresenter

interface ISignInPresenter : IBaseFragmentMvpPresenter<ISignInView> {
    fun login(userName: String, password: String)
}