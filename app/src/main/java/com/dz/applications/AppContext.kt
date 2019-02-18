package com.dz.applications

import com.dz.di.AppComponent
import com.dz.di.AppModule
import com.dz.di.DaggerAppComponent
import com.dz.libraries.applications.ExtApplication
import com.dz.libraries.loggers.Logger
import com.dz.libraries.preferences.AppPreferences
import com.dz.libraries.rxbus.IEvent
import com.dz.libraries.rxbus.RxBus
import com.dz.libraries.utilities.GsonUtility
import com.dz.libraries.utilities.NetworkUtility
import com.dz.libraries.utilities.TypefaceUtility
import com.dz.libraries.views.recyclerviews.ExtRecyclerView
import com.dz.models.responses.SignInResponse
import com.dz.ui.BuildConfig
import com.dz.ui.R
import com.dz.utilities.Constant
import com.dz.utilities.FragmentUtility
import io.reactivex.exceptions.UndeliverableException
import io.reactivex.plugins.RxJavaPlugins
import javax.inject.Inject

class AppContext : ExtApplication() {
    companion object {
        private const val TAG = "AppContext"

        private var signInResponse: SignInResponse? = null

        fun isAuthorized(): Boolean = signInResponse != null

        fun getAccessToken(): String? = signInResponse?.accessToken

        fun getTokenType(): String? = "Bearer"

        fun setData(response: SignInResponse?) {
            signInResponse = response
            AppPreferences.setValue(Constant.SIGN_IN_RESPONSE, GsonUtility.getGsonBuilder().toJson(response))
        }

        fun logOut() = setData(null)
    }

    // application component
    lateinit var appComponent: AppComponent

    @Inject
    lateinit var rxBus: RxBus<IEvent>

    override fun onCreate() {
        super.onCreate()

        // Dagger is a fully static, compile-time dependency injection framework for both Java and Android.
        // It is an adaptation of an earlier version created by Square and now maintained by Google.
        // Dagger aims to address many of the development and performance issues that have plagued reflection-based solutions.
        appComponent = DaggerAppComponent
                .builder()
                .appModule(AppModule(this))
                .build()
        appComponent.inject(this)

        // logger
        Logger.setEnableLog(true)

        // catch all exception when error occur in app
        if (!BuildConfig.DEBUG) {
            Thread.setDefaultUncaughtExceptionHandler { thread, e -> Logger.e("$TAG::${thread.javaClass.name}", e.message) }
        }

        FragmentUtility.containerId = R.id.flContainer

        // paging number per page
        ExtRecyclerView.numberPerPage = Constant.NUMBER_PER_PAGE

        // https://github.com/Polidea/RxAndroidBle/wiki/FAQ:-UndeliverableException
        RxJavaPlugins.setErrorHandler { error ->
            if (error is UndeliverableException) {
                return@setErrorHandler // ignore BleExceptions as they were surely delivered at least once
            }
            // add other custom handlers if needed
            throw error
        }

        // set font default
        TypefaceUtility.FONT_DEFAULT = getString(R.string.font_normal_display)

        // overwrite all font
        TypefaceUtility.overrideFont(this, "SERIF", getString(R.string.font_normal_display))

        // update sign in info
        setData(GsonUtility.getGsonBuilder().fromJson(AppPreferences.getValue(Constant.SIGN_IN_RESPONSE, String::class, ""),
                SignInResponse::class.java))

        // trust all certificate
        NetworkUtility.nuke()
    }
}