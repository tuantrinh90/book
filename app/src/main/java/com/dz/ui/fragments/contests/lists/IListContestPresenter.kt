package com.dz.ui.fragments.contests.lists

import com.dz.commons.presenters.IBaseFragmentMvpPresenter

interface IListContestPresenter : IBaseFragmentMvpPresenter<IListContestView> {
    fun getData(status: Int, sortByTime: String, sortByStatus: String, offset: Int)
}