package com.dz.utilities

import android.util.Log
import com.dz.commons.views.IBaseActivityMvpView
import com.dz.commons.views.IBaseFragmentMvpView
import com.dz.commons.views.IBaseMvpView
import com.dz.libraries.loggers.Logger
import com.dz.libraries.rxbus.IEvent
import com.dz.libraries.rxbus.RxBus
import com.dz.libraries.utilities.OptionalUtility
import com.dz.listeners.IApiCallback
import com.dz.models.BaseResponse
import com.dz.models.events.NetworkEvent
import com.trello.rxlifecycle3.LifecycleTransformer
import com.trello.rxlifecycle3.android.ActivityEvent
import com.trello.rxlifecycle3.android.FragmentEvent
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

fun <T> Observable<T>.delayEach(interval: Long, timeUnit: TimeUnit): Observable<T> =
        this.concatMap { i -> Observable.just(i).delay(interval, timeUnit) }

class RxUtility {
    companion object {
        private const val TAG = "RxUtility"

        /**
         * Use to get api restful using retrofit
         *
         * @param v           is use to get lifecycle of fragment
         * @param observable  is response of retrofit when request api
         * @param apiCallback is use to callback data to ui view
         * @param <T>         is class parse response
         * @return Disposable to cancel subscribe with lifecycle onDestroy of fragment */
        fun <T> async(v: IBaseMvpView, observable: Observable<BaseResponse<T>>,
                      apiCallback: IApiCallback<T>? = null) {
            if (!v.isNetworkAvailable()) {
                v.rxBus.send(NetworkEvent(ErrorCodes.NO_INTERNET))
                return
            }

            // bind to lifecycle
            when (v) {
                is IBaseFragmentMvpView -> observable.compose(v.bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                is IBaseActivityMvpView -> observable.compose(v.bindUntilEvent(ActivityEvent.DESTROY))
            }

            // start service
            apiCallback?.onStart()
            v.getCompositeDisposable().add(observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : DisposableObserver<BaseResponse<T>>() {
                        override fun onNext(response: BaseResponse<T>) {
                            OptionalUtility.with(response).doIfPresent { r ->
                                if (r.success) {
                                    apiCallback?.onSuccess(r.data)
                                } else {
                                    val error = ErrorUtility.getBaseErrorText(v.appContext, v.rxBus, ErrorUtility.getErrorBodyApp(r.message))
                                    Logger.e(TAG, "onError:: $error")
                                    apiCallback?.onError(error)
                                }
                            }
                        }

                        override fun onError(e: Throwable) {
                            val error = ErrorUtility.getBaseErrorText(v.appContext, v.rxBus, ErrorUtility.createErrorBody(e))
                            Log.e(TAG, "onError:: $error")
                            apiCallback?.onError(error)
                            apiCallback?.onFinish()
                        }

                        override fun onComplete() {
                            apiCallback?.onFinish()
                        }
                    }))
        }

        fun <T : IEvent> registerRxBus(rxBus: RxBus<IEvent>, mCompositeDisposable: CompositeDisposable, clazz: Class<T>,
                                       lifecycleProvider: LifecycleTransformer<T>, consumer: (T) -> Unit) {
            mCompositeDisposable.add(rxBus.ofType(clazz).compose(lifecycleProvider)
                    .subscribeWith(object : DisposableObserver<T>() {
                        override fun onNext(t: T) = consumer(t)
                        override fun onError(e: Throwable) {}
                        override fun onComplete() {}
                    }))
        }

        fun disposeComposite(mCompositeDisposable: CompositeDisposable) {
            if (!mCompositeDisposable.isDisposed) {
                mCompositeDisposable.dispose()
            }
        }
    }
}