package com.dz.commons.presenters

import android.os.Bundle
import com.dz.commons.views.IBaseMvpView
import com.dz.di.AppComponent
import com.dz.interactors.IDataModule
import com.dz.libraries.rxbus.IEvent
import com.dz.libraries.rxbus.RxBus
import com.dz.utilities.RxUtility
import io.reactivex.disposables.CompositeDisposable
import java.lang.ref.WeakReference
import javax.inject.Inject

abstract class BaseMvpPresenter<V : IBaseMvpView>(appComponent: AppComponent) : IBaseMvpPresenter<V> {
    @Inject
    lateinit var dataModule: IDataModule

    // This is an event rxBus designed to allowing your application to communicate efficiently.
    @Inject
    lateinit var rxBus: RxBus<IEvent>

    // view
    var viewRef: WeakReference<V>? = null

    // rx java
    val mCompositeDisposable = CompositeDisposable()

    // inject di
    init {
        @Suppress("UNCHECKED_CAST")
        appComponent.inject(this as BaseMvpPresenter<IBaseMvpView>)
    }

    override fun attachView(view: V) {
        viewRef = WeakReference(view)
    }

    override fun isViewAttached(): Boolean = viewRef != null && viewRef!!.get() != null

    override fun getView(consumer: (v: V) -> Unit) {
        if (!isViewAttached()) return
        consumer(viewRef!!.get()!!)
    }

    override fun detachView() {
        // dispose rxjava
        RxUtility.disposeComposite(mCompositeDisposable)

        // remove view
        viewRef?.clear()
        viewRef = null
    }

    override fun processArguments(arguments: Bundle?) {}

    override fun saveInstanceState(bundle: Bundle?) {}

    override fun restoreInstanceState(bundle: Bundle?) {}
}