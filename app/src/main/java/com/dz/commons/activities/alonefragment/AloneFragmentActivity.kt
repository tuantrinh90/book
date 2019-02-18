package com.dz.commons.activities.alonefragment

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.WindowManager
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import butterknife.BindView
import com.dz.commons.Keys
import com.dz.commons.activities.BaseAppCompatActivity
import com.dz.libraries.loggers.Logger
import com.dz.libraries.utilities.BarUtility
import com.dz.libraries.utilities.OptionalUtility
import com.dz.ui.R
import com.google.android.material.appbar.AppBarLayout

class AloneFragmentActivity : BaseAppCompatActivity<IAloneFragmentActivityView, IAloneFragmentActivityPresenter>(), IAloneFragmentActivityView {
    companion object {
        private const val TAG = "AloneFragmentActivity"
        private const val FRAGMENT_NAME = "fragment_name"
        private const val TRANSLUCENT = "translucent"

        /**
         * @param context
         * @return
         */
        fun with(context: Context): Builder {
            return Builder(context)
        }

        /**
         * @param fragment
         * @return
         */
        fun with(fragment: Fragment): Builder {
            return Builder(fragment)
        }
    }

    @BindView(R.id.ablAppBarLayout)
    lateinit var ablAppBarLayout: AppBarLayout

    @BindView(R.id.tbToolbar)
    lateinit var tbToolbar: Toolbar

    // fragment
    private var fragment: Fragment? = null

    override val contentViewId: Int get() = R.layout.alone_fragment_activity

    override val appSupportActionBar: ActionBar? get() = supportActionBar

    override val appBarLayout: AppBarLayout? get() = ablAppBarLayout

    override val appToolBar: Toolbar? get() = tbToolbar

    override fun createPresenter(): IAloneFragmentActivityPresenter = AloneFragmentActivityPresenter(appComponent)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // translucent status bar
        if (intent?.extras!!.getBoolean(TRANSLUCENT)) {
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            appBarLayout?.setPadding(0, BarUtility.getStatusBarHeight(this), 0, 0)
        }

        // set up support action bar
        setSupportActionBar(tbToolbar)

        // show fragment
        getFragmentForOpen(intent?.extras!!) { fr -> replaceFragment(fr, false) }
    }

    /**
     * @param bundle
     * @return bundle from builder
     */
    private fun getArgsForFragment(bundle: Bundle): Bundle {
        return bundle.getBundle(Keys.ARGS)!!
    }

    /**
     * @param bundle
     * @param fragmentForOpen
     */
    private fun getFragmentForOpen(bundle: Bundle, fragmentForOpen: (Fragment) -> Unit) {
        fragmentForOpen(Fragment.instantiate(applicationContext, bundle.getString(FRAGMENT_NAME), getArgsForFragment(bundle)))
    }

    /**
     * @param f       will be display in screen
     * @param addToBackStack if true it will add fragment to back stack to serve when user implement action back
     */
    private fun replaceFragment(f: Fragment, addToBackStack: Boolean) {
        try {
            fragment = f
            val replace = supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.flContainer, f)
            if (addToBackStack) {
                replace.addToBackStack(null)
            }
            replace.commitAllowingStateLoss()
        } catch (e: Exception) {
            Logger.e(TAG, e)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            // for handle in fragment first
            if (!OptionalUtility.isNullOrEmpty(fragment)) {
                if (fragment!!.onOptionsItemSelected(item)) {
                    return true
                }
            }

            setResult(Activity.RESULT_CANCELED)
            finish()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    class Builder {
        private val contextForOpen: Context?
        private val fragmentForOpen: Fragment?
        private var translucent: Boolean = false
        private var overrideAnim: Boolean = false
        private var requestCode: Int? = null
        private var params: Bundle? = null

        constructor(context: Context) {
            this.contextForOpen = context
            this.fragmentForOpen = null
        }

        constructor(fragment: Fragment) {
            this.fragmentForOpen = fragment
            this.contextForOpen = null
        }

        fun setStatusTransluent(translucent: Boolean): Builder {
            this.translucent = translucent
            return this
        }

        fun overrideStartAnimation(override: Boolean): Builder {
            this.overrideAnim = override
            return this
        }

        fun forResult(requestCode: Int): Builder {
            this.requestCode = requestCode
            return this
        }

        fun parameters(params: Bundle): Builder {
            this.params = params
            return this
        }

        private fun getIntent(contextForOpen: Context?, fragmentForOpen: Fragment?, params: Bundle?): Intent {
            val intent = Intent(contextForOpen
                    ?: fragmentForOpen!!.context, AloneFragmentActivity::class.java)
            intent.putExtra(Keys.ARGS, params ?: Bundle())
            return intent
        }

        private fun createIntentForStart(fragmentClass: Class<out Fragment>): Intent {
            val intent = getIntent(contextForOpen, fragmentForOpen, params)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra(FRAGMENT_NAME, fragmentClass.name)
            intent.putExtra(TRANSLUCENT, translucent)
            return intent
        }

        fun start(fragmentClass: Class<out Fragment>) {
            try {
                val intent = createIntentForStart(fragmentClass)
                if (contextForOpen != null) {
                    if (requestCode != null) {
                        (contextForOpen as Activity).startActivityForResult(intent, requestCode!!)
                    } else {
                        contextForOpen.startActivity(intent)
                    }
                } else {
                    if (requestCode != null) {
                        fragmentForOpen!!.startActivityForResult(intent, requestCode!!)
                    } else {
                        fragmentForOpen!!.startActivity(intent)
                    }
                }

                if (overrideAnim) {
                    if (contextForOpen != null) {
                        val realContext: Context = when (contextForOpen) {
                            is Activity -> contextForOpen
                            is ContextWrapper -> contextForOpen.baseContext
                            else -> contextForOpen
                        }

                        (realContext as Activity).overridePendingTransition(R.anim.slide_up_bottom_to_top, R.anim.stay)
                    } else {
                        fragmentForOpen!!.activity!!.overridePendingTransition(R.anim.slide_up_bottom_to_top, R.anim.stay)
                    }
                }
            } catch (e: Exception) {
                Logger.e(TAG, e)
            }
        }
    }
}