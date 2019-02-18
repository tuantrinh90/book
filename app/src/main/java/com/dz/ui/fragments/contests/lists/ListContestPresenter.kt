package com.dz.ui.fragments.contests.lists

import com.dz.commons.presenters.BaseFragmentMvpPresenter
import com.dz.di.AppComponent
import com.dz.libraries.utilities.StringUtility
import com.dz.listeners.ApiCallback
import com.dz.models.ArrayResponse
import com.dz.models.responses.ContestResponse
import com.dz.utilities.Constant
import com.dz.utilities.RxUtility

class ListContestPresenter(appComponent: AppComponent) : BaseFragmentMvpPresenter<IListContestView>(appComponent), IListContestPresenter {
    override fun getData(status: Int, sortByTime: String, sortByStatus: String, offset: Int) {
        getView {
            val params = HashMap<String, String>()
            params["offset"] = "$offset"
            params["limit"] = "${Constant.NUMBER_PER_PAGE}"
            params["sort"] = "[{\"field\":\"endDate\",\"direction\":\"$sortByTime\"}]"
            params["filter[status]"] = "$status"

            // filter
            StringUtility.with(sortByStatus)
                    .doIfPresent { j -> params["joined"] = j }

            RxUtility.async(it, dataModule.apiService.getContests(params),
                    object : ApiCallback<ArrayResponse<ContestResponse>>() {
                        override fun onStart() {
                            it.showLoadingPagingView(true)
                        }

                        override fun onFinish() {
                            it.showLoadingPagingView(false)
                        }

                        override fun onSuccess(t: ArrayResponse<ContestResponse>?) {
                            it.setData(t)
                        }

                        override fun onError(error: String?) {
                            it.showToastError(error)
                        }
                    })
        }
    }
}