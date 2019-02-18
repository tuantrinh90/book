package com.dz.ui.fragments.menus

import com.dz.commons.presenters.BaseFragmentMvpPresenter
import com.dz.di.AppComponent

class MenuPresenter(appComponent: AppComponent) : BaseFragmentMvpPresenter<IMenuView>(appComponent), IMenuPresenter {
}