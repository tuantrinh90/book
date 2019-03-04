package com.dz.ui.fragments.history

import android.content.Context
import com.dz.commons.presenters.BaseFragmentMvpPresenter
import com.dz.di.AppComponent
import com.dz.models.database.Book
import com.dz.models.responses.BookResponse
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class DetailPresenter(appComponent: AppComponent, mContext: Context) : BaseFragmentMvpPresenter<IDetailView>(appComponent), IDetailPresenter {
    override fun getData() {

    }

    override fun getBookById(id: Int) {
        getView {
            Observable.fromCallable({
                dataModule.getBookbyID(id)

            }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribeWith(object : DisposableObserver<Book>() {
                override fun onComplete() {}

                override fun onNext(t: Book) {
                    it.getBook(t)
                }

                override fun onError(e: Throwable) {
                }
            })

        }
    }

    override fun updateFavorite(book: Book) {
        Observable.fromCallable({
            dataModule.updateBook(book)
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribeWith(object : DisposableObserver<Unit>() {
            override fun onComplete() {}

            override fun onNext(t: Unit) {
            }

            override fun onError(e: Throwable) {
            }
        })

    }


}