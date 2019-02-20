package com.dz.ui.fragments.history

import com.dz.commons.presenters.BaseFragmentMvpPresenter
import com.dz.di.AppComponent
import com.dz.models.responses.BookResponse

class HistoryPresenter(appComponent: AppComponent) : BaseFragmentMvpPresenter<IHistoryView>(appComponent), IHistoryPresenter {
    override fun getBook() {
        getView {
            var books = ArrayList<BookResponse?>()
            var book = BookResponse()
            book.link = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTokoT2fdDP5dm7RvVh80_LvoxdhF4By5DkcliO4yukgMMOeRZj"
            book.author = "Thích Giác Nhàn"
            book.name = "Vô lượng thọ"
            book.categoryId = 0

            var book1 = BookResponse()
            book1.link = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTokoT2fdDP5dm7RvVh80_LvoxdhF4By5DkcliO4yukgMMOeRZj"
            book1.author = "Hòa thượng tinh không"
            book1.name = "Tải kinh a di đà"
            book1.categoryId = 0

            var book2 = BookResponse()
            book2.link = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTokoT2fdDP5dm7RvVh80_LvoxdhF4By5DkcliO4yukgMMOeRZj"
            book2.author = "Pháp sư văn sao"
            book2.name = "Ấn quang pháp"
            book2.categoryId = 0

            books.add(book)
            books.add(book1)
            books.add(book2)
            books.add(book)
            books.add(book)
            books.add(book)

            it.setData(books)
        }
    }

}