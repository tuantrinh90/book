package com.dz.ui.activities.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import butterknife.BindView
import com.dz.commons.activities.BaseAppCompatActivity
import com.dz.commons.fragments.BaseMvpFragment
import com.dz.customizes.views.edittexts.EditTextApp
import com.dz.libraries.utilities.StringUtility
import com.dz.ui.R
import com.dz.ui.adapters.FragmentViewPagerAdapter
import com.dz.ui.fragments.history.*
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout

class MainActivity : BaseAppCompatActivity<IMainActivityView, IMainActivityPresenter>(), IMainActivityView {

    @BindView(R.id.ablAppBarLayout)
    lateinit var ablAppBarLayout: AppBarLayout
    @BindView(R.id.tbToolbar)
    lateinit var tbToolbar: Toolbar
    @BindView(R.id.tlTabLayout)
    lateinit var tlTabLayout: TabLayout
    @BindView(R.id.vpViewPager)
    lateinit var vpViewPager: ViewPager


    override val contentViewId: Int get() = com.dz.ui.R.layout.main_activity

    override val appSupportActionBar: ActionBar? get() = supportActionBar

    override val appBarLayout: AppBarLayout? get() = ablAppBarLayout

    override val appToolBar: Toolbar? get() = tbToolbar

    override fun onBackPressed() = onBackPressedAction()

    override fun createPresenter(): IMainActivityPresenter = MainActivityPresenter(appComponent)

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        registerBus()
    }

    override fun initFragmentDefault() {

    }

    fun registerBus() {

    }

    fun initView() {
        val titles = ArrayList<String>()
        titles.add(getString(R.string.history))
        titles.add(getString(R.string.favorite))
        titles.add(getString(R.string.download))
        titles.add(getString(R.string.infomation))

        val fragments = ArrayList<BaseMvpFragment<*, *>>()
        fragments.add(HistoryFragment()
                .setChildFragment(true))
        fragments.add(FavoriteFragment()
                .setChildFragment(true))
        fragments.add(DownloadFragment()
                .setChildFragment(true))
        fragments.add(InfomationFragment()
                .setChildFragment(true))

        vpViewPager.adapter = FragmentViewPagerAdapter(supportFragmentManager, fragments, titles)
        tlTabLayout.setupWithViewPager(vpViewPager)
    }


}
