package com.dz.ui.fragments.contests

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.viewpager.widget.ViewPager
import butterknife.BindView
import com.dz.commons.fragments.BaseMvpFragment
import com.dz.ui.R
import com.dz.ui.adapters.FragmentViewPagerAdapter
import com.dz.ui.fragments.BaseMainFragment
import com.dz.ui.fragments.contests.lists.ListContestFragment
import com.google.android.material.tabs.TabLayout

class ContestFragment : BaseMainFragment<IContestView, IContestPresenter>(), IContestView {
    @BindView(R.id.tlTabLayout)
    lateinit var tlTabLayout: TabLayout
    @BindView(R.id.vpViewPager)
    lateinit var vpViewPager: ViewPager

    override fun createPresenter(): IContestPresenter = ContestPresenter(appComponent)

    override val resourceId: Int get() = R.layout.contest_fragment

    override val titleId: Int get() = R.string.contest

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindButterKnife(view)
        initViews()
    }

    fun initViews() {
        val titles = ArrayList<String>()
        titles.add(getString(R.string.open))
        titles.add(getString(R.string.close))

        val fragments = ArrayList<BaseMvpFragment<*, *>>()
        fragments.add(ListContestFragment()
                .setCompositeDisposable(mCompositeDisposable)
                .setType(ListContestFragment.Type.OPEN)
                .setChildFragment(true))
        fragments.add(ListContestFragment()
                .setCompositeDisposable(mCompositeDisposable)
                .setType(ListContestFragment.Type.CLOSE)
                .setChildFragment(true))

        vpViewPager.adapter = FragmentViewPagerAdapter(fragmentManager!!, fragments, titles)
        tlTabLayout.setupWithViewPager(vpViewPager)
    }

    override fun initToolbar(supportActionBar: ActionBar) {
        super.initToolbar(supportActionBar)
        // elevation
        mActivity.appBarLayout?.elevation = 0f
    }
}