package com.dz.commons.views

import com.dz.applications.AppContext
import com.dz.di.AppComponent
import com.dz.libraries.rxbus.IEvent
import com.dz.libraries.rxbus.RxBus
import com.trello.rxlifecycle3.LifecycleTransformer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

interface IBaseMvpView {
    val appContext: AppContext

    val appComponent: AppComponent

    var rxBus: RxBus<IEvent>

    fun getCompositeDisposable(): CompositeDisposable

    fun isNetworkAvailable(): Boolean

    fun showLoading(isLoading: Boolean)

    fun showMessage(message: String?)

    fun showMessage(message: String?, ok: String)

    fun showMessage(message: String?, ok: String, consumer: () -> Unit)

    fun showMessage(message: String?, ok: String, cancel: String, okConsumer: () -> Unit, cancelConsumer: () -> Unit)

    fun showLoadingPagingView(isLoading: Boolean)

    fun showLoadingView(isLoading: Boolean)

    fun <T : IEvent> registerRxBus(clazz: Class<T>, lifecycleProvider: LifecycleTransformer<T>, consumer: (T) -> Unit)

    fun subscribeWith(consumer: () -> Disposable)
}