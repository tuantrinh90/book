package com.dz.ui.fragments.history

import com.dz.commons.views.IBaseFragmentMvpView
import com.dz.models.responses.BookResponse

interface IHistoryView : IBaseFragmentMvpView {
    fun setData(response: ArrayList<BookResponse?>?)
}