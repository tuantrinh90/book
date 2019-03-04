package com.dz.di

import com.dz.applications.AppContext
import com.dz.interactors.IDataModule
import com.dz.interactors.databases.IDbDao
import com.dz.interactors.services.IApiService
import com.dz.interactors.services.IFileApiService
import com.dz.interactors.services.ILongApiService
import com.dz.interactors.databases.IDbModule
import com.dz.interactors.repository.BookRepository
import com.dz.libraries.rxbus.IEvent
import com.dz.libraries.rxbus.RxBus
import com.dz.libraries.utilities.NetworkUtility
import com.dz.models.database.Book
import dagger.Module
import javax.inject.Inject

@Module
class DataModule(component: AppComponent) : IDataModule {

    @Inject
    lateinit var rxBus: RxBus<IEvent>

    @Inject
    override lateinit var apiService: IApiService

    @Inject
    override lateinit var longApiService: ILongApiService

    @Inject
    override lateinit var fileApiService: IFileApiService

    @Inject
    override lateinit var dbModule: IDbModule

    @Inject
    lateinit var context: AppContext

    init {
        component.inject(this)
    }

    lateinit var bookRepository: BookRepository

    override fun getBookbyID(id: Int): Book {
        bookRepository = BookRepository(intanceDao())
        return bookRepository.getBookByID(id)
    }

    override fun updateBook(book: Book) {
        bookRepository = BookRepository(intanceDao())
        bookRepository.updateBook(book)
    }

    override fun intanceDao(): IDbDao {
        var db: AppData? = AppData.getAppData(context)
        return db!!.idbDao()
    }

    override fun getBooks(): List<Book> {
        bookRepository = BookRepository(intanceDao())
        return bookRepository.getBooks
    }

    override fun getFavoriteBook(): List<Book> {
        bookRepository = BookRepository(intanceDao())
        return bookRepository.getFavoriteBook()
    }

    override fun intanceBook(): List<Book> {
        bookRepository = BookRepository(intanceDao())
        var books = ArrayList<Book>()
        if (NetworkUtility.isNetworkAvailable(context)) {

            var book = Book(
                    1, "Vô lượng thọ"
                    , "Nam mô a di đà phật",
                    "Thích Giác Nhàn",
                    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTokoT2fdDP5dm7RvVh80_LvoxdhF4By5DkcliO4yukgMMOeRZj", false, 1, "Loại A", "Xuất bản trẻ Việt Nam")
            bookRepository.addNewBook(book)
            books.add(book)
            var book1 = Book(
                    2, "Vô lượng thọ 1 "
                    , "Nam mô a di đà phật",
                    "Hòa thượng tinh không",
                    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTokoT2fdDP5dm7RvVh80_LvoxdhF4By5DkcliO4yukgMMOeRZj", false, 1, "Loại A", "Xuất bản trẻ Việt Nam")
            bookRepository.addNewBook(book1)
            books.add(book1)
            var book2 = Book(
                    3, "Vô lượng thọ 1 "
                    , "Nam mô a di đà phật",
                    "Hòa thượng tinh không",
                    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTokoT2fdDP5dm7RvVh80_LvoxdhF4By5DkcliO4yukgMMOeRZj", false, 1, "Loại A", "Xuất bản trẻ Việt Nam")
            bookRepository.addNewBook(book2)
            books.add(book2)
            var book3 = Book(
                    4, "Vô lượng thọ 1 "
                    , "Nam mô a di đà phật",
                    "Hòa thượng tinh không",
                    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTokoT2fdDP5dm7RvVh80_LvoxdhF4By5DkcliO4yukgMMOeRZj", false, 1, "Loại A", "Xuất bản trẻ Việt Nam")
            bookRepository.addNewBook(book3)
            books.add(book3)
            var book4 = Book(
                    5, "Vô lượng thọ 1 "
                    , "Nam mô a di đà phật",
                    "Hòa thượng tinh không",
                    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTokoT2fdDP5dm7RvVh80_LvoxdhF4By5DkcliO4yukgMMOeRZj", false, 1, "Loại A", "Xuất bản trẻ Việt Nam")
            bookRepository.addNewBook(book4)
            books.add(book4)
        } else {
            books = getBooks() as ArrayList<Book>
        }
        return books

    }

    override fun deeleteBook(book: Book) {
        bookRepository = BookRepository(intanceDao())
        bookRepository.deleteBook(book)
    }


}

