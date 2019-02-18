package com.dz.commons.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.annotation.CheckResult
import androidx.annotation.StringRes
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import butterknife.ButterKnife
import butterknife.Unbinder
import com.dz.applications.AppContext
import com.dz.commons.fragments.BaseMvpFragment
import com.dz.commons.presenters.IBaseActivityMvpPresenter
import com.dz.commons.presenters.IBaseFragmentMvpPresenter
import com.dz.commons.views.IBaseActivityMvpView
import com.dz.commons.views.IBaseFragmentMvpView
import com.dz.di.AppComponent
import com.dz.libraries.rxbus.IEvent
import com.dz.libraries.rxbus.RxBus
import com.dz.libraries.utilities.*
import com.dz.libraries.views.textviews.ExtTextView
import com.dz.models.events.NetworkEvent
import com.dz.ui.BuildConfig
import com.dz.ui.R
import com.dz.utilities.ErrorCodes
import com.dz.utilities.FragmentUtility
import com.dz.utilities.RxUtility
import com.google.android.material.appbar.AppBarLayout
import com.tbruyelle.rxpermissions2.RxPermissions
import com.trello.rxlifecycle3.LifecycleTransformer
import com.trello.rxlifecycle3.RxLifecycle
import com.trello.rxlifecycle3.android.ActivityEvent
import com.trello.rxlifecycle3.android.RxLifecycleAndroid
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import java.util.*
import javax.inject.Inject

abstract class BaseAppCompatActivity<V : IBaseActivityMvpView, P : IBaseActivityMvpPresenter<V>> : AppCompatActivity(), IBaseActivityMvpView {
    companion object {
        private const val TAG = "BaseAppCompatActivity"
    }

    // app context
    @Inject
    override lateinit var rxBus: RxBus<IEvent>

    // rx permission
    lateinit var rxPermissions: RxPermissions

    lateinit var presenter: P

    // get view id to update content view
    abstract val contentViewId: Int

    // app support action bar
    open val appSupportActionBar: ActionBar? = null

    // app bar layout at top of screen
    open val appBarLayout: AppBarLayout? = null

    // toolbar
    open val appToolBar: Toolbar? = null

    // app context
    override val appContext: AppContext get() = applicationContext as AppContext

    override val appComponent: AppComponent get() = appContext.appComponent

    // create presenter
    abstract fun createPresenter(): P

    // rxjava dispose subscribe
    val mCompositeDisposable = CompositeDisposable()

    // store fragment in back stack
    val fragments: Stack<BaseMvpFragment<IBaseFragmentMvpView, IBaseFragmentMvpPresenter<IBaseFragmentMvpView>>> = Stack()

    // Fragments have a different view lifecycle than activities.
    // When binding a fragment in onCreateView, set the views to null in onDestroyView.
    // Butter Knife returns an Unbinder instance when you call bind to do this for you.
    // Call its unbind method in the appropriate lifecycle callback.
    var unbinder: Unbinder? = null

    // title app
    /**
     * @return a appComponent to display title in app at action bar
     */
    var titleToolBar: ExtTextView? = null

    // message
    var vMessage: View? = null

    // main content
    var vMainContent: View? = null

    // loading view
    var vLoadingView: View? = null

    // network
    var connectionStateMonitor: ConnectionStateMonitor? = null

    // lifecycle subject
    val lifecycleSubject = BehaviorSubject.create<ActivityEvent>()

    @CheckResult
    override fun lifecycle(): Observable<ActivityEvent> = lifecycleSubject.hide()

    /**
     * Binds a source until a specific event occurs.
     *
     * @param event the event that triggers un-subscription
     * @return a reusable [LifecycleTransformer] which un-subscribes when the event triggers.
     */
    @CheckResult
    override fun <T> bindUntilEvent(event: ActivityEvent): LifecycleTransformer<T> = RxLifecycle.bindUntilEvent(lifecycleSubject, event)

    /**
     * Binds a source until the next reasonable event occurs.
     *
     * @return a reusable [LifecycleTransformer] which un-subscribes at the correct time.
     */
    @CheckResult
    override fun <T> bindToLifecycle(): LifecycleTransformer<T> = RxLifecycleAndroid.bindActivity(lifecycleSubject)

    @Suppress("UNCHECKED_CAST")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // create presenter
        presenter = createPresenter()
        presenter.attachView(this as V)

        // lifecycle
        lifecycleSubject.onNext(ActivityEvent.CREATE)

        // inject appComponent
        appContext.appComponent.inject(this as BaseAppCompatActivity<IBaseActivityMvpView, IBaseActivityMvpPresenter<IBaseActivityMvpView>>)

        // rx permission
        rxPermissions = RxPermissions(this)
        rxPermissions.setLogging(BuildConfig.DEBUG)

        // set content view
        setContentView(contentViewId)

        // bind view
        unbinder = ButterKnife.bind(this)

        // connection state monitor
        connectionStateMonitor = ConnectionStateMonitor(this, rxBus)

        // view message
        val vMessageId = resources.getIdentifier("vMessage", "id", packageName)
        if (vMessageId > 0) {
            vMessage = findViewById(vMessageId)
        }

        // loading view
        val vLoadingViewId = resources.getIdentifier("llLoadingView", "id", packageName)
        if (vLoadingViewId > 0) {
            vLoadingView = findViewById(vLoadingViewId)
        }

        // main content
        val vMainContentId = resources.getIdentifier("flContainer", "id", packageName)
        if (vMainContentId > 0) {
            vMainContent = findViewById(vMainContentId)
        }

        // show loading view
        showLoadingView(false)

        // get title toolbar
        appToolBar?.let {
            val toolbarTitleId = resources.getIdentifier("tvToolbarTitle", "id", packageName)
            if (toolbarTitleId > 0) titleToolBar = it.findViewById(toolbarTitleId)
        }

        // init fragment default
        initFragmentDefault()

        // rxBus listener
        registerNetworkEventListener()

        // dont show keyboard
        KeyboardUtility.dontShowKeyboard(this)
    }

    @Suppress("DEPRECATION")
    override fun onStart() {
        super.onStart()

        // lifecycle
        lifecycleSubject.onNext(ActivityEvent.START)

        // register listener network
        connectionStateMonitor?.enable()
    }

    override fun onResume() {
        super.onResume()

        // lifecycle
        lifecycleSubject.onNext(ActivityEvent.RESUME)
    }

    override fun onPause() {
        super.onPause()

        // lifecycle
        lifecycleSubject.onNext(ActivityEvent.PAUSE)
    }

    override fun onStop() {
        // unregister listener network
        connectionStateMonitor?.disable()

        // lifecycle
        lifecycleSubject.onNext(ActivityEvent.STOP)

        super.onStop()
    }

    override fun onDestroy() {
        // lifecycle
        lifecycleSubject.onNext(ActivityEvent.DESTROY)

        // hide key board
        KeyboardUtility.hideSoftKeyboard(this)

        // close dialog
        DialogUtility.dismiss()

        // close loading
        ProgressBarDialogUtility.dismiss()

        // dispose rxjava
        RxUtility.disposeComposite(mCompositeDisposable)

        // unbind butter knife
        OptionalUtility.with(unbinder).doIfPresent { it.unbind() }

        // unbind presenter
        OptionalUtility.with(presenter).doIfPresent { it.detachView() }

        // clear network monitor state
        connectionStateMonitor = null

        // destroy
        super.onDestroy()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var result = false

        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                result = true
            }
        }

        return result || super.onOptionsItemSelected(item)
    }

    /**
     * register bus
     */
    override fun <T : IEvent> registerRxBus(clazz: Class<T>, lifecycleProvider: LifecycleTransformer<T>, consumer: (T) -> Unit) {
        RxUtility.registerRxBus(rxBus, mCompositeDisposable, clazz, lifecycleProvider, consumer)
    }

    // add dispose rxjava
    override fun subscribeWith(consumer: () -> Disposable) {
        mCompositeDisposable.add(consumer())
    }

    override fun onBackPressed() {
        super.onBackPressed()
        KeyboardUtility.dontShowKeyboard(this)
    }

    /**
     * show loading in view
     *
     * @param isLoading
     */
    override fun showLoadingView(isLoading: Boolean) {
        vLoadingView?.visibility = if (isLoading) View.VISIBLE else View.GONE
        vMainContent?.visibility = if (isLoading) View.INVISIBLE else View.VISIBLE
    }

    override fun getCompositeDisposable(): CompositeDisposable = mCompositeDisposable

    override fun isNetworkAvailable(): Boolean = NetworkUtility.isNetworkAvailable(this)

    override fun showLoading(isLoading: Boolean) = if (isLoading) {
        showProgressDialog()
    } else {
        hideProgressDialog()
    }

    /**
     * Use to display message for user
     *
     * @param message is the message the application wants to tell the user
     */
    override fun showMessage(message: String?) {
        DialogUtility.messageBox(this, getString(R.string.app_name), message, getString(R.string.ok))
    }

    /**
     * Use to display message for user
     *
     * @param message is the message the application wants to tell the user
     * @param ok      is label display for button ok from string.xml
     */
    override fun showMessage(message: String?, ok: String) {
        DialogUtility.messageBox(this, getString(R.string.app_name), message, ok)
    }

    /**
     * Use to display message for user
     *
     * @param message  is the message the application wants to tell the user
     * @param ok       is label display for button ok from string.xml
     * @param consumer will be use to callback when user click button ok
     */
    override fun showMessage(message: String?, ok: String, consumer: () -> Unit) {
        DialogUtility.messageBox(this, getString(R.string.app_name), message, ok, DialogInterface.OnClickListener { _, _ -> consumer() })
    }

    /**
     * Use to display message for user
     *
     * @param message        is the message the application wants to tell the user
     * @param ok             is label display for button ok from string.xml
     * @param cancel         is label display for button cancel from string.xml
     * @param okConsumer     will be use to callback when user click button ok
     * @param cancelConsumer will be use to callback when user click button cancel
     */
    override fun showMessage(message: String?, ok: String, cancel: String, okConsumer: () -> Unit, cancelConsumer: () -> Unit) {
        DialogUtility.messageBox(this, getString(R.string.app_name), message, ok, cancel, DialogInterface.OnClickListener { _, _ -> okConsumer() },
                DialogInterface.OnClickListener { _, _ -> cancelConsumer() })
    }

    override fun showLoadingPagingView(isLoading: Boolean) {}

    /**
     * Use to display default fragment in activity
     */
    open fun initFragmentDefault() {}

    // network listener
    fun registerNetworkEventListener() {
        registerRxBus(NetworkEvent::class.java, bindUntilEvent(ActivityEvent.DESTROY)) {
            when (it.errorCode) {
                ErrorCodes.UNKNOWN -> {
                }
                ErrorCodes.NO_INTERNET -> vMessage?.visibility = View.VISIBLE
                ErrorCodes.AVAILABLE_INTERNET -> vMessage?.visibility = View.GONE
                ErrorCodes.TIME_OUT -> {
                }
                ErrorCodes.UPDATING -> {
                }
                ErrorCodes.APP_ERROR -> {
                }
                ErrorCodes.UNAUTHORIZED -> {
//                    AppContext.logOut()
//                    val intent = Intent(this@BaseAppCompatActivity, SignInActivity::class.java)
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
//                    startActivity(intent)
                }
                ErrorCodes.FORBIDDEN -> {
                }
                ErrorCodes.NOT_FOUND -> {
                }
                ErrorCodes.SERVER_ERROR -> {
                }
                ErrorCodes.BAD_GATEWAY -> {
                }
            }
        }
    }

    @SuppressLint("ResourceType")
    fun setToolbarTitle(@StringRes titleId: Int) {
        setToolbarTitle(if (titleId <= 0) "" else getString(titleId))
    }

    fun setToolbarTitle(title: String) {
        titleToolBar?.text = title
    }

    // show loading
    fun showProgressDialog() {
        ProgressBarDialogUtility.setMessage(getString(R.string.loading))
        ProgressBarDialogUtility.show(this)
    }

    // hide loading
    fun hideProgressDialog() = ProgressBarDialogUtility.dismiss()

    // back action
    private fun onClickBackAction() {
        KeyboardUtility.dontShowKeyboard(this)
        if (CollectionUtility.with(fragments).size() > 1) {
            fragments.pop()
            val fragment = fragments.peek()
            OptionalUtility.with(fragment).doIfPresent { FragmentUtility.replaceFragment(this, it) }
        } else {
            clearStackFragment()
            initFragmentDefault()
        }
    }

    // clear stack fragment
    fun clearStackFragment() = fragments.clear()

    // on back press
    fun onBackPressedAction() {
        CollectionUtility.with(fragments).doIfEmpty { BackUtility.onClickExit(this, getString(R.string.double_tap_to_exit)) }
                .doIfPresent { onClickBackAction() }
    }

    /**
     * network listener
     */
    class ConnectionStateMonitor(var context: Context, var rxBus: RxBus<IEvent>) : ConnectivityManager.NetworkCallback() {
        var networkRequest: NetworkRequest = NetworkRequest.Builder()
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .build()

        var connectivityManager = context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager

        override fun onAvailable(network: Network?) {
            super.onAvailable(network)
            rxBus.send(NetworkEvent(ErrorCodes.AVAILABLE_INTERNET))
            Log.e("onAvailable", "onAvailable")
        }

        override fun onUnavailable() {
            super.onUnavailable()
            // rxBus.send(NetworkEvent(ErrorCodes.NO_INTERNET))
            Log.e("onUnavailable", "onUnavailable")
        }

        override fun onLost(network: Network?) {
            super.onLost(network)
            rxBus.send(NetworkEvent(ErrorCodes.NO_INTERNET))
            Log.e("onLost", "onLost")
        }

        fun enable() {
            connectivityManager?.registerNetworkCallback(networkRequest, this)
        }

        fun disable() {
            connectivityManager?.unregisterNetworkCallback(this)
        }
    }
}
