package com.dz.commons.fragments

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.CheckResult
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.DialogFragment
import butterknife.ButterKnife
import butterknife.Unbinder
import com.dz.applications.AppContext
import com.dz.commons.activities.BaseAppCompatActivity
import com.dz.commons.fragments.toast.ToastDialogFragment
import com.dz.commons.presenters.IBaseFragmentMvpPresenter
import com.dz.commons.views.IBaseFragmentMvpView
import com.dz.di.AppComponent
import com.dz.interactors.IDataModule
import com.dz.libraries.models.IModel
import com.dz.libraries.rxbus.IEvent
import com.dz.libraries.rxbus.RxBus
import com.dz.libraries.utilities.DialogUtility
import com.dz.libraries.utilities.KeyboardUtility
import com.dz.libraries.utilities.NetworkUtility
import com.dz.libraries.utilities.OptionalUtility
import com.dz.libraries.views.recyclerviews.ExtRecyclerView
import com.dz.libraries.views.recyclerviews.ExtRecyclerViewHolder
import com.dz.models.responses.UploadResponse
import com.dz.ui.R
import com.dz.utilities.RxUtility
import com.trello.rxlifecycle3.LifecycleTransformer
import com.trello.rxlifecycle3.RxLifecycle
import com.trello.rxlifecycle3.android.FragmentEvent
import com.trello.rxlifecycle3.android.RxLifecycleAndroid
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

abstract class BaseMvpDialogFragment<V : IBaseFragmentMvpView, P : IBaseFragmentMvpPresenter<V>> : DialogFragment(), IBaseFragmentMvpView {
    @Inject
    override lateinit var rxBus: RxBus<IEvent>

    @Inject
    lateinit var dataModule: IDataModule

    lateinit var mActivity: BaseAppCompatActivity<*, *>

    // presenter
    lateinit var presenter: P

    // dispose rxjava
    var mCompositeDisposable = CompositeDisposable()

    // Fragments have a different view lifecycle than activities.
    // When binding a fragment in onCreateView, set the views to null in onDestroyView.
    // Butter Knife returns an Unbinder instance when you call bind to do this for you.
    // Call its unbind method in the appropriate lifecycle callback.
    var unbinder: Unbinder? = null

    // This variable is used to identify embedded fragments in the viewpager
    // It does not change title, icon.. of parent fragment
    var isChildFragment = false

    // lifecycle subject
    val lifecycleSubject = BehaviorSubject.create<FragmentEvent>()

    // create presenter
    abstract fun createPresenter(): P

    override val titleId: Int get() = 0

    override val titleString: String get() = ""

    override val appContext: AppContext get() = mActivity.appContext

    override val appComponent: AppComponent get() = appContext.appComponent

    override fun getCompositeDisposable(): CompositeDisposable = mCompositeDisposable

    @CheckResult
    override fun lifecycle(): Observable<FragmentEvent> = lifecycleSubject.hide()

    /**
     * Binds a source until a specific event occurs.
     *
     * @param event the event that triggers un-subscription
     * @return a reusable [LifecycleTransformer] which un-subscribes when the event triggers.
     */
    @CheckResult
    override fun <T> bindUntilEvent(event: FragmentEvent): LifecycleTransformer<T> = RxLifecycle.bindUntilEvent(lifecycleSubject, event)

    /**
     * Binds a source until the next reasonable event occurs.
     *
     * @return a reusable [LifecycleTransformer] which un-subscribes at the correct time.
     */
    @CheckResult
    override fun <T> bindToLifecycle(): LifecycleTransformer<T> = RxLifecycleAndroid.bindFragment(lifecycleSubject)

    /**
     * on attach fragment
     */
    override fun onAttach(context: Context?) {
        super.onAttach(context)

        // lifecycle
        lifecycleSubject.onNext(FragmentEvent.ATTACH)

        // instance of activity
        if (context is BaseAppCompatActivity<*, *>) mActivity = context
    }

    @Suppress("UNCHECKED_CAST")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // create presenter
        presenter = createPresenter()
        presenter.processArguments(savedInstanceState)

        // lifecycle
        lifecycleSubject.onNext(FragmentEvent.CREATE)

        // inject appComponent
        appContext.appComponent.inject(this as BaseMvpDialogFragment<IBaseFragmentMvpView, IBaseFragmentMvpPresenter<IBaseFragmentMvpView>>)

        // retain this fragment when activity is re-initialized
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // lifecycle
        lifecycleSubject.onNext(FragmentEvent.CREATE_VIEW)

        return inflater.inflate(resourceId, container, false)
    }

    @Suppress("UNCHECKED_CAST")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // attach view to presenter
        presenter.attachView(this as V)

        // hide keyboard
        KeyboardUtility.hideSoftKeyboard(mActivity, view)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        presenter.restoreInstanceState(savedInstanceState)
        super.onViewStateRestored(savedInstanceState)
    }

    @CallSuper
    override fun onStart() {
        super.onStart()

        // lifecycle
        lifecycleSubject.onNext(FragmentEvent.START)
    }

    @CallSuper
    override fun onResume() {
        super.onResume()

        // lifecycle
        lifecycleSubject.onNext(FragmentEvent.RESUME)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        presenter.saveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    @CallSuper
    override fun onPause() {
        // lifecycle
        lifecycleSubject.onNext(FragmentEvent.PAUSE)

        super.onPause()
    }

    @CallSuper
    override fun onStop() {
        // lifecycle
        lifecycleSubject.onNext(FragmentEvent.STOP)

        super.onStop()
    }

    override fun onDestroyView() {
        // lifecycle
        lifecycleSubject.onNext(FragmentEvent.DESTROY_VIEW)

        // hide loading
        showLoading(false)

        // hide keyboard
        KeyboardUtility.hideSoftKeyboard(mActivity)

        // unbind butter knife
        OptionalUtility.with(unbinder).doIfPresent { it.unbind() }

        // unbind event
        OptionalUtility.with(presenter).doIfPresent { it.detachView() }

        // dispose rxjava
        RxUtility.disposeComposite(mCompositeDisposable)

        super.onDestroyView()
    }

    @CallSuper
    override fun onDestroy() {
        // lifecycle
        lifecycleSubject.onNext(FragmentEvent.DESTROY)

        super.onDestroy()
    }

    @CallSuper
    override fun onDetach() {
        // lifecycle
        lifecycleSubject.onNext(FragmentEvent.DETACH)

        super.onDetach()
    }

    override fun <T : IEvent> registerRxBus(clazz: Class<T>, lifecycleProvider: LifecycleTransformer<T>, consumer: (T) -> Unit) {
        RxUtility.registerRxBus(rxBus, mCompositeDisposable, clazz, lifecycleProvider, consumer)
    }

    override fun subscribeWith(consumer: () -> Disposable) {
        mCompositeDisposable.add(consumer())
    }

    override fun bindButterKnife(view: View) {
        OptionalUtility.with(view).doIfPresent { unbinder = ButterKnife.bind(this, it) }
    }

    /**
     * Use to set toolbar default for each screen
     * Overwrite this method when need change something
     *
     * @param supportActionBar: use to change actions on toolbar
     */
    override fun initToolbar(supportActionBar: ActionBar) {}

    // network state
    override fun isNetworkAvailable(): Boolean = NetworkUtility.isNetworkAvailable(mActivity)

    /**
     * show toast message error
     */
    override fun showToastError(message: String?, dismissConsumer: (() -> Unit)?) {
        ToastDialogFragment.showMessage(fragmentManager, ToastDialogFragment.ToastType.ERROR, message, dismissConsumer)
    }

    /**
     * show toast message info
     */
    override fun showToastInfo(message: String?, dismissConsumer: (() -> Unit)?) {
        ToastDialogFragment.showMessage(fragmentManager, ToastDialogFragment.ToastType.INFO, message, dismissConsumer)
    }

    /**
     * show toast message success
     */
    override fun showToastSuccess(message: String?, dismissConsumer: (() -> Unit)?) {
        ToastDialogFragment.showMessage(fragmentManager, ToastDialogFragment.ToastType.SUCCESS, message, dismissConsumer)
    }

    /**
     * show toast message warn
     */
    override fun showToastWarm(message: String?, dismissConsumer: (() -> Unit)?) {
        ToastDialogFragment.showMessage(fragmentManager, ToastDialogFragment.ToastType.WARN, message, dismissConsumer)
    }

    /**
     * Use to display message for user
     *
     * @param message is the message the application wants to tell the user
     */
    override fun showMessage(message: String?) {
        DialogUtility.messageBox(mActivity, getString(R.string.app_name), message, getString(R.string.ok))
    }

    /**
     * Use to display message for user
     *
     * @param message is the message the application wants to tell the user
     * @param ok      is label display for button ok from string.xml
     */
    override fun showMessage(message: String?, ok: String) {
        DialogUtility.messageBox(mActivity, getString(R.string.app_name), message, ok)
    }

    /**
     * Use to display message for user
     *
     * @param message  is the message the application wants to tell the user
     * @param ok       is label display for button ok from string.xml
     * @param consumer will be use to callback when user click button ok
     */
    override fun showMessage(message: String?, ok: String, consumer: () -> Unit) {
        DialogUtility.messageBox(mActivity, getString(R.string.app_name), message, ok, DialogInterface.OnClickListener { _, _ -> consumer() })
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
        DialogUtility.messageBox(mActivity, getString(R.string.app_name), message, ok, cancel, DialogInterface.OnClickListener { _, _ -> okConsumer() },
                DialogInterface.OnClickListener { _, _ -> cancelConsumer() })
    }

    /**
     * upload file success
     */
    override fun onUploadSuccess(response: UploadResponse?) {}

    override fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            mActivity.showProgressDialog()
        } else {
            mActivity.hideProgressDialog()
        }
    }

    // get paging list view
    open fun getExtPagingListView(): ExtRecyclerView<IModel, ExtRecyclerViewHolder>? = null

    // show/hide loading in ext recycler view
    override fun showLoadingPagingView(isLoading: Boolean) {
        OptionalUtility.with(getExtPagingListView()).doIfPresent {
            if (isLoading) {
                it.showLoading()
            } else {
                it.hideLoading()
            }
        }
    }

    // show loading in view
    override fun showLoadingView(isLoading: Boolean) {}
}