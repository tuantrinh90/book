package com.dz.ui.fragments.hashtags.dialogs

import com.dz.commons.views.IBaseFragmentMvpView
import com.dz.models.responses.HashTagResponse

interface IHashTagActionView : IBaseFragmentMvpView {
    fun onSuccess(response: ArrayList<HashTagResponse?>?)
}