package com.dz.ui.fragments.hashtags

import com.dz.commons.presenters.BaseFragmentMvpPresenter
import com.dz.di.AppComponent
import com.dz.libraries.utilities.StringUtility
import com.dz.listeners.ApiCallback
import com.dz.models.ArrayResponse
import com.dz.models.responses.HashTagResponse
import com.dz.utilities.Constant
import com.dz.utilities.RxUtility

class HashTagPresenter(appComponent: AppComponent) : BaseFragmentMvpPresenter<IHashTagView>(appComponent), IHashTagPresenter {
    override fun getHashTags(query: String, offset: Int) {
        getView {
            val params = HashMap<String, String>()
            params["limit"] = "${Constant.NUMBER_PER_PAGE}"
            params["offset"] = "$offset"
            StringUtility.with(query).doIfPresent { q -> params["filter[name]"] = q }

            RxUtility.async(it, dataModule.apiService.getHashTags(params),
                    object : ApiCallback<ArrayResponse<HashTagResponse>>() {
                        override fun onStart() {
                            it.showLoadingPagingView(true)
                        }

                        override fun onFinish() {
                            it.showLoadingPagingView(false)
                        }

                        override fun onSuccess(t: ArrayResponse<HashTagResponse>?) {
                            it.setData(t?.items)
                        }

                        override fun onError(error: String?) {
                            it.showToastError(error)
                        }
                    })
        }
    }
}