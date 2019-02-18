package com.dz.ui.fragments.accounts.forgotpasswords

import com.dz.commons.presenters.BaseFragmentMvpPresenter
import com.dz.di.AppComponent

class ForgotPasswordPresenter(appComponent: AppComponent) : BaseFragmentMvpPresenter<IForgotPasswordView>(appComponent), IForgotPasswordPresenter