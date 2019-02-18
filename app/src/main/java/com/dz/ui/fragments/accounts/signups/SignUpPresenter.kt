package com.dz.ui.fragments.accounts.signups

import com.dz.commons.presenters.BaseFragmentMvpPresenter
import com.dz.di.AppComponent

class SignUpPresenter(appComponent: AppComponent) : BaseFragmentMvpPresenter<ISignUpView>(appComponent), ISignUpPresenter