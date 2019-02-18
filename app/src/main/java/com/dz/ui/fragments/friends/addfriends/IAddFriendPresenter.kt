package com.dz.ui.fragments.friends.addfriends

import com.dz.commons.presenters.IBaseFragmentMvpPresenter

interface IAddFriendPresenter : IBaseFragmentMvpPresenter<IAddFriendView> {
    fun getFriends(query: String, offset: Int)
}