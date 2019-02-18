package com.dz.ui.fragments.friends.addfriends

import com.dz.commons.presenters.BaseFragmentMvpPresenter
import com.dz.di.AppComponent
import com.dz.libraries.utilities.StringUtility
import com.dz.listeners.ApiCallback
import com.dz.models.ArrayResponse
import com.dz.models.responses.MemberResponse
import com.dz.utilities.Constant
import com.dz.utilities.RxUtility

class AddFriendPresenter(appComponent: AppComponent) : BaseFragmentMvpPresenter<IAddFriendView>(appComponent), IAddFriendPresenter {
    override fun getFriends(query: String, offset: Int) {
        getView {
            val params = HashMap<String, String>()
            params["limit"] = "${Constant.NUMBER_PER_PAGE}"
            params["offset"] = "$offset"
            StringUtility.with(query).doIfPresent { q -> params["name"] = q }

            RxUtility.async(it, dataModule.apiService.getFriends(params),
                    object : ApiCallback<ArrayResponse<MemberResponse>>() {
                        override fun onStart() {
                            it.showLoadingPagingView(true)
                        }

                        override fun onFinish() {
                            it.showLoadingPagingView(false)
                        }

                        override fun onSuccess(t: ArrayResponse<MemberResponse>?) {
                            it.setData(t?.items)
                        }

                        override fun onError(error: String?) {
                            it.showToastError(error)
                        }
                    })
        }
    }
}