package com.dz.ui.activities.splash

import android.content.Intent
import android.os.Bundle
import com.dz.applications.AppContext
import com.dz.commons.activities.BaseAppCompatActivity
import com.dz.libraries.utilities.ActivityUtility
import com.dz.ui.R
import com.dz.ui.activities.main.MainActivity
import com.dz.utilities.Constant
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import java.util.concurrent.TimeUnit

class SplashActivity : BaseAppCompatActivity<ISplashActivityView, ISplashActivityPresenter>() {
    override val contentViewId: Int get() = R.layout.splash_activity

    override fun createPresenter(): ISplashActivityPresenter = SplashActivityPresenter(appComponent)

    override fun onCreate(savedInstanceState: Bundle?) {
        ActivityUtility.setFullScreen(this)
        super.onCreate(savedInstanceState)

        // go to main activity
        MainActivity.fragmentState = MainActivity.FragmentState.HOME
        subscribeWith {
            Observable.timer(Constant.TIME_DELAY_NEXT_SCREEN, TimeUnit.SECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : DisposableObserver<Long>() {
                        override fun onComplete() {
                            val intent = Intent(this@SplashActivity, MainActivity::class.java)
                            // val intent = Intent(this@SplashActivity, SignInActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }

                        override fun onNext(t: Long) {}

                        override fun onError(e: Throwable) {}
                    })
        }
    }
}