package com.dz.interactors.databases

import androidx.room.*
import com.dz.models.database.Book
import com.dz.models.database.Category
import com.dz.models.database.LinkBook
import com.dz.models.database.PageBook

@Dao
interface IDbDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBook(book: Book)

    @Delete
    fun deleteBook(book: Book?)

    @Update
    fun updateBook(book: Book?)

    @Query("SELECT * FROM Book where id = :id")
    fun getBookById(id: Int): Book

    @Query("SELECT * FROM Book where favorite = 1")
    fun getFavoriteBook(): List<Book>

    @Query("SELECT * FROM Book")
    fun getBooks(): List<Book>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCategory(category: Category?)

    @Delete
    fun deleteCategory(category: Category?)

    @Update
    fun updateCategory(category: Category?)

    @Query("SELECT * FROM Category where id = :id")
    fun getCategoryById(id: Int): Category

    @Query("SELECT * FROM Category")
    fun getCategorys(): List<Category>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPageBook(pageBook: PageBook?)

    @Delete
    fun deletePageBook(pageBook: PageBook?)

    @Update
    fun updatePageBook(pageBook: PageBook?)

    @Query("SELECT * FROM PageBook where book_id = :id")
    fun getPageBookById(id: Int): List<PageBook>

    @Query("DELETE FROM PageBook where book_id = :id")
    fun deletePageBooks(id: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLinkBook(linkBook: LinkBook?)

    @Delete
    fun deleteLinkBook(linkBook: LinkBook?)

    @Update
    fun updateLinkBook(linkBook: LinkBook?)

    @Query("SELECT * FROM LinkBook where book_id = :id")
    fun getLinkBookById(id: Int): List<LinkBook?>

    @Query("DELETE FROM LinkBook where book_id = :id")
    fun deleteLinkBooks(id: Int)

}