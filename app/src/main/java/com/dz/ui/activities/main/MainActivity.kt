package com.dz.ui.activities.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.Toolbar
import butterknife.BindView
import butterknife.OnClick
import com.dz.commons.activities.BaseAppCompatActivity
import com.dz.customizes.views.footers.FooterItem
import com.dz.ui.R
import com.dz.ui.fragments.contests.ContestFragment
import com.dz.ui.fragments.homes.HomeFragment
import com.dz.ui.fragments.justaminutes.JustAMinuteFragment
import com.dz.ui.fragments.menus.MenuFragment
import com.dz.utilities.Constant
import com.dz.utilities.FragmentUtility
import com.dz.utilities.delayEach
import com.google.android.material.appbar.AppBarLayout
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class MainActivity : BaseAppCompatActivity<IMainActivityView, IMainActivityPresenter>(), IMainActivityView {
    companion object {
        // default fragment
        var fragmentState = FragmentState.CONTEST
    }

    enum class FragmentState { HOME, JUST_A_MINUTE, CONTEST, MENU }

    @BindView(R.id.ablAppBarLayout)
    lateinit var ablAppBarLayout: AppBarLayout
    @BindView(R.id.tbToolbar)
    lateinit var tbToolbar: Toolbar
    @BindView(R.id.llFooter)
    lateinit var llFooter: LinearLayout
    @BindView(R.id.fiHome)
    lateinit var fiHome: FooterItem
    @BindView(R.id.fiJustMinute)
    lateinit var fiJustMinute: FooterItem
    @BindView(R.id.fiHomeMenu)
    lateinit var fiHomeMenu: AppCompatImageView
    @BindView(R.id.fiContest)
    lateinit var fiContest: FooterItem
    @BindView(R.id.fiMenu)
    lateinit var fiMenu: FooterItem

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
        subscribeWith {
            Observable.just("")
                    .delayEach(Constant.TIME_DELAY_INIT_SCREEN, TimeUnit.MILLISECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { switchTab() }
        }
    }

    fun registerBus() {

    }

    fun initView() {
        setSupportActionBar(tbToolbar)
    }

    fun switchTab() {
        clearStackFragment()
        when (fragmentState) {
            FragmentState.HOME -> FragmentUtility.replaceFragment(this, HomeFragment())
            FragmentState.JUST_A_MINUTE -> FragmentUtility.replaceFragment(this, JustAMinuteFragment())
            FragmentState.CONTEST -> FragmentUtility.replaceFragment(this, ContestFragment())
            FragmentState.MENU -> FragmentUtility.replaceFragment(this, MenuFragment())
        }
        activeFooterItem()
    }

    fun activeFooterItem() {
        fiHome.setActiveMode(this, false)
        fiJustMinute.setActiveMode(this, false)
        fiContest.setActiveMode(this, false)
        fiMenu.setActiveMode(this, false)

        when (fragmentState) {
            FragmentState.HOME -> fiHome.setActiveMode(this, true)
            FragmentState.JUST_A_MINUTE -> fiJustMinute.setActiveMode(this, true)
            FragmentState.CONTEST -> fiContest.setActiveMode(this, true)
            FragmentState.MENU -> fiMenu.setActiveMode(this, true)
        }
    }

    @OnClick(R.id.fiHome)
    fun onClickHome() {
        fragmentState = FragmentState.HOME
        switchTab()
    }

    @OnClick(R.id.fiJustMinute)
    fun onClickJustMinute() {
        fragmentState = FragmentState.JUST_A_MINUTE
        switchTab()
    }

    @OnClick(R.id.fiHomeMenu)
    fun onClickHomeMenu() {

    }

    @OnClick(R.id.fiContest)
    fun onClickContest() {
        fragmentState = FragmentState.CONTEST
        switchTab()
    }

    @OnClick(R.id.fiMenu)
    fun onClickMenu() {
        fragmentState = FragmentState.MENU
        switchTab()
    }
}
