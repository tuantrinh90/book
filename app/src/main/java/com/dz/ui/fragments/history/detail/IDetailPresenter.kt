package com.dz.ui.fragments.history

import com.dz.commons.presenters.IBaseFragmentMvpPresenter

interface IDetailPresenter : IBaseFragmentMvpPresenter<IDetailView> {
    fun getData()
}