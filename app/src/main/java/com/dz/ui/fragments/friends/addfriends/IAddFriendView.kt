package com.dz.ui.fragments.friends.addfriends

import com.dz.commons.views.IBaseFragmentMvpView
import com.dz.models.responses.MemberResponse

interface IAddFriendView : IBaseFragmentMvpView {
    fun setData(response: ArrayList<MemberResponse?>?)
}