package com.dz.ui.fragments.upload

import com.dz.commons.views.IBaseFragmentMvpView
import com.dz.models.responses.SubmisstionResponse

interface IUploadVideoView : IBaseFragmentMvpView {
    fun onSubmisstion(response: SubmisstionResponse?)
}