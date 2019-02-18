package com.dz.ui.fragments.upload

import com.dz.commons.presenters.BaseFragmentMvpPresenter
import com.dz.di.AppComponent
import com.dz.listeners.ApiCallback
import com.dz.models.requests.SubmissionRequest
import com.dz.models.responses.SubmisstionResponse
import com.dz.utilities.RxUtility

class UploadVideoPresenter(appcomponent: AppComponent) : BaseFragmentMvpPresenter<IUploadVideoView>(appcomponent), IUploadVideoPresenter {

    override fun getSubmisstion(video: String?, videoThumb: String?, images: ArrayList<String?>?,
                                description: String?, contestId: Int?,
                                hashtas: ArrayList<Int?>?, friends: ArrayList<Int?>?) {
        getView {
            RxUtility.async(it, dataModule.apiService.submissionFile(SubmissionRequest(description, contestId, friends, images, video, videoThumb)),
                    object : ApiCallback<SubmisstionResponse>() {
                        override fun onSuccess(t: SubmisstionResponse?) {
                            it.onSubmisstion(t)
                        }

                        override fun onError(error: String?) {
                            it.showToastError(error)
                        }

                        override fun onFinish() {
                            it.showLoading(false)
                        }

                        override fun onStart() {
                            it.showLoading(true)
                        }
                    })
        }
    }
}