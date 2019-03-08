package com.dz.ui.fragments.history

import com.dz.commons.views.IBaseFragmentMvpView
import com.dz.models.BookDetail
import com.dz.models.database.Book
import com.dz.models.responses.BookResponse

interface IDetailView : IBaseFragmentMvpView {

    fun setData(response: ArrayList<BookResponse?>?)

    fun getBook(book: Book)

    fun getBookDetail(bookDetail: List<BookDetail?>?)
}