package com.dz.interactors

import com.dz.interactors.databases.IDbDao
import com.dz.interactors.services.IApiService
import com.dz.interactors.services.IFileApiService
import com.dz.interactors.services.ILongApiService
import com.dz.interactors.databases.IDbModule
import com.dz.models.BookDetail
import com.dz.models.database.Book

interface IDataModule {
    var apiService: IApiService

    var longApiService: ILongApiService

    var fileApiService: IFileApiService

    var dbModule: IDbModule

    fun getBooks(): List<Book>

    fun intanceDao(): IDbDao

    fun deeleteBook(book: Book)

    fun intanceBook(): List<Book>

    fun updateBook(book: Book)

    fun getBookbyID(id: Int): Book

    fun getFavoriteBook(): List<Book>

    fun getDownloadedBook(): List<Book>

    fun getBookDetail(): List<BookDetail>

}
