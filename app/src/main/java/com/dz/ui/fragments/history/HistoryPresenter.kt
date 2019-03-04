package com.dz.ui.fragments.history

import com.dz.commons.presenters.BaseFragmentMvpPresenter
import com.dz.di.AppComponent
import com.dz.models.database.Book
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class HistoryPresenter(appComponent: AppComponent) : BaseFragmentMvpPresenter<IHistoryView>(appComponent), IHistoryPresenter {
    override fun getBook() {
        getView {
            Observable.fromCallable({
                dataModule.intanceBook()

            }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribeWith(object : DisposableObserver<List<Book>>() {
                override fun onComplete() {}

                override fun onNext(t: List<Book>) {
                    it.setData(t)
                }

                override fun onError(e: Throwable) {
                }
            })

        }
    }

}