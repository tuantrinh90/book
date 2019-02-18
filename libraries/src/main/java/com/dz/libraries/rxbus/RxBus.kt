package com.dz.libraries.rxbus

import android.os.Handler
import android.os.Looper
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class RxBus<T : IEvent> {
    companion object {
        private const val TAG = "RxBus"
    }

    // publish subject rxjava
    private val mPublishSubject = PublishSubject.create<T>()

    // handle data
    private val handler = Handler(Looper.getMainLooper())

    /**
     * @param clazz
     * @param
     * @return a observable listener event in app  */
    fun <A : IEvent> ofType(clazz: Class<A>): Observable<A> = mPublishSubject.ofType(clazz)

    /**
     * Send event to broadcast.
     *
     * @param event event to be caught.
     */
    fun send(event: T) = handler.post { mPublishSubject.onNext(event) }
}
