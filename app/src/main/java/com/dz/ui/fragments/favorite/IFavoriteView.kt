package com.dz.ui.fragments.history

import com.dz.commons.views.IBaseFragmentMvpView
import com.dz.models.database.Book

interface IFavoriteView : IBaseFragmentMvpView {
    fun setData(response: List<Book?>?)
}