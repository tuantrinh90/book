package com.dz.ui.fragments.hashtags

import com.dz.commons.views.IBaseFragmentMvpView
import com.dz.models.responses.HashTagResponse

interface IHashTagView : IBaseFragmentMvpView {
    fun setData(response: ArrayList<HashTagResponse?>?)
}