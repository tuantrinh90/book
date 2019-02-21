package com.dz.ui.fragments.history

import com.dz.commons.presenters.BaseFragmentMvpPresenter
import com.dz.di.AppComponent

class FavoritePresenter(appComponent: AppComponent) : BaseFragmentMvpPresenter<IFavoriteView>(appComponent), IFavoritePresenter {
}