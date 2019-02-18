package com.dz.ui.fragments.contests.lists

import com.dz.commons.views.IBaseFragmentMvpView
import com.dz.models.ArrayResponse
import com.dz.models.responses.ContestResponse

interface IListContestView : IBaseFragmentMvpView {
    fun setData(response: ArrayResponse<ContestResponse>?)
}