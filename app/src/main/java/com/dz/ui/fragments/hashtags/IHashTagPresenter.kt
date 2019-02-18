package com.dz.ui.fragments.hashtags

import com.dz.commons.presenters.IBaseFragmentMvpPresenter

interface IHashTagPresenter : IBaseFragmentMvpPresenter<IHashTagView> {
    fun getHashTags(query: String, offset: Int)
}