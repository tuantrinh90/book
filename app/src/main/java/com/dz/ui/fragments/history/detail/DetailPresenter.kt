package com.dz.ui.fragments.history

import com.dz.commons.presenters.BaseFragmentMvpPresenter
import com.dz.di.AppComponent
import com.dz.models.responses.BookResponse

class DetailPresenter(appComponent: AppComponent) : BaseFragmentMvpPresenter<IDetailView>(appComponent), IDetailPresenter {
    override fun getData() {
        getView {
            var books = ArrayList<BookResponse?>()
            var book = BookResponse()
            book.link = "https://www.youtube.com/watch?time_continue=3&v=E6OiF0cUOp4"
            book.name = "Khai Giảng Lớp Đệ Tử Quy năm 2019 tại Tịnh Thất Quan Âm"
            books.add(book)
            books.add(book)
            books.add(book)
            books.add(book)
            books.add(book)
            books.add(book)
            books.add(book)
            it.setData(books)
        }
    }
}