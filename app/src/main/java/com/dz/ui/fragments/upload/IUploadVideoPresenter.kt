package com.dz.ui.fragments.upload

import com.dz.commons.presenters.IBaseFragmentMvpPresenter

interface IUploadVideoPresenter : IBaseFragmentMvpPresenter<IUploadVideoView> {
    fun getSubmisstion(video: String?, videoThumb: String?,
                       images: ArrayList<String?>?, description: String?,
                       contestId: Int?, hashtas: ArrayList<Int?>?,
                       friends: ArrayList<Int?>?)
}