package com.dz.ui.fragments.history

import com.dz.commons.presenters.IBaseFragmentMvpPresenter

interface IHistoryPresenter : IBaseFragmentMvpPresenter<IHistoryView> {
    fun getBook()
}