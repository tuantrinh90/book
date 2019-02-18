package com.dz.utilities

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.dz.commons.fragments.BaseMvpFragment
import com.dz.commons.presenters.IBaseFragmentMvpPresenter
import com.dz.commons.views.IBaseFragmentMvpView
import com.dz.libraries.utilities.CollectionUtility

class FragmentUtility {
    companion object {
        private const val TAG = "FragmentUtility"
        var containerId = 0

        /**
         * @param activity
         * @param fragment
         */
        fun <V : IBaseFragmentMvpView, P : IBaseFragmentMvpPresenter<V>> replaceFragment(activity: FragmentActivity, fragment: BaseMvpFragment<V, P>?) = replaceFragment(activity, fragment, null)

        /**
         * @param activity
         * @param fragment
         * @param consumer
         */
        fun <V : IBaseFragmentMvpView, P : IBaseFragmentMvpPresenter<V>> replaceFragment(activity: FragmentActivity,
                                                                                         fragment: BaseMvpFragment<V, P>?, consumer: ((BaseMvpFragment<V, P>) -> Unit)? = null) {
            if (containerId <= 0) throw NullPointerException()
            replaceFragment(activity, containerId, fragment, consumer)
        }

        /**
         * @param activity
         * @param containerViewId
         * @param fragment
         */
        fun <V : IBaseFragmentMvpView, P : IBaseFragmentMvpPresenter<V>> replaceFragment(activity: FragmentActivity,
                                                                                         containerViewId: Int, fragment: BaseMvpFragment<V, P>?) = replaceFragment(activity, containerViewId, fragment, null)

        /**
         * @param activity
         * @param containerViewId
         * @param fragment
         * @param consumer
         */
        fun <V : IBaseFragmentMvpView, P : IBaseFragmentMvpPresenter<V>> replaceFragment(activity: FragmentActivity, containerViewId: Int,
                                                                                         fragment: BaseMvpFragment<V, P>? = null, consumer: ((BaseMvpFragment<V, P>) -> Unit)? = null) {
            fragment?.let {
                activity.supportFragmentManager
                        .beginTransaction()
                        .replace(containerViewId, it)
                        //.addToBackStack(null)
                        .commitAllowingStateLoss()
                consumer?.invoke(it)
            }
        }

        /**
         * clear cache fragment in view pager
         *
         * @param activity
         * @param fragments
         */
        fun <V : IBaseFragmentMvpView, P : IBaseFragmentMvpPresenter<V>> clearCacheFragment(activity: FragmentActivity, fragments: MutableList<BaseMvpFragment<V, P>?>?) {
            CollectionUtility.with(fragments).doIfPresent {
                val fragmentManager = activity.supportFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()
                for (fragment in it) fragment?.let { f -> fragmentTransaction.remove(f) }
                fragmentTransaction.commitAllowingStateLoss()
                fragments!!.clear()
            }
        }

        /**
         * clear fragment in cache
         *
         * @param activity
         * @param fragment
         */
        fun <V : IBaseFragmentMvpView, P : IBaseFragmentMvpPresenter<V>> clearCacheFragment(activity: FragmentActivity, fragment: BaseMvpFragment<V, P>?) {
            fragment?.let {
                run {
                    val fragmentManager = activity.supportFragmentManager
                    val fragmentTransaction = fragmentManager.beginTransaction()
                    fragmentTransaction.remove(it)
                    fragmentTransaction.commitAllowingStateLoss()
                }
            }
        }

        fun <V : IBaseFragmentMvpView, P : IBaseFragmentMvpPresenter<V>, F : BaseMvpFragment<V, P>> newInstance(clazz: Class<F>, bundle: Bundle? = null): F {
            val fragment = clazz.newInstance()
            bundle?.let { fragment.arguments = bundle }
            return fragment
        }
    }
}