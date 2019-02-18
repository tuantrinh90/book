package com.dz.ui.fragments.hashtags.dialogs

import com.dz.commons.presenters.BaseFragmentMvpPresenter
import com.dz.di.AppComponent
import com.dz.libraries.utilities.GsonUtility
import com.dz.listeners.ApiCallback
import com.dz.models.responses.HashTagResponse
import com.dz.utilities.RxUtility

class HashTagActionPresenter(appComponent: AppComponent) : BaseFragmentMvpPresenter<IHashTagActionView>(appComponent), IHashTagActionPresenter {
    override fun createHashTag(name: String) {
        getView {
            RxUtility.async(it, dataModule.apiService.createHashTag(GsonUtility.getGsonBuilder().toJson(arrayListOf(name))),
                    object : ApiCallback<ArrayList<HashTagResponse?>>() {
                        override fun onStart() {
                            it.showLoading(true)
                        }

                        override fun onFinish() {
                            it.showLoading(false)
                        }

                        override fun onSuccess(t: ArrayList<HashTagResponse?>?) {
                            it.onSuccess(t)
                        }

                        override fun onError(error: String?) {
                            it.showToastError(error)
                        }
                    })
        }
    }
}