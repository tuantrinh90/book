package com.dz.interactors.repository

import com.dz.interactors.databases.IDbDao
import com.dz.models.database.Book

class BookRepository(mIdbDao: IDbDao) {

    val iDbDao: IDbDao = mIdbDao
    val getBooks: List<Book> = iDbDao.getBooks()

    fun addNewBook(book: Book) {
        if (getBookByID(book.id) == null)
            iDbDao.insertBook(book)
    }

    fun getFavoriteBook(): List<Book> {
        return iDbDao.getFavoriteBook()
    }

    fun getDownloadedBook(): List<Book> {
        return iDbDao.getDownloadedBook()
    }

    fun getBookByID(id: Int) = iDbDao.getBookById(id)

    fun deleteBook(book: Book) {
        iDbDao.deleteLinkBooks(book.id)
        iDbDao.deletePageBooks(book.id)
        iDbDao.deleteBook(book)
    }

    fun updateBook(book: Book) = iDbDao.updateBook(book)

}