package com.dz.ui.fragments.hashtags.dialogs

import com.dz.commons.presenters.IBaseFragmentMvpPresenter

interface IHashTagActionPresenter : IBaseFragmentMvpPresenter<IHashTagActionView> {
    fun createHashTag(name: String)
}