package com.dz.ui.fragments.history

import com.dz.commons.presenters.IBaseFragmentMvpPresenter
import com.dz.models.database.Book

interface IDetailPresenter : IBaseFragmentMvpPresenter<IDetailView> {

    fun getData()

    fun updateFavorite(book: Book)

    fun getBookById(id: Int)
}