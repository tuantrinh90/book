package com.dz.ui.fragments.justaminutes

import android.os.Bundle
import android.view.View
import com.dz.ui.R
import com.dz.ui.fragments.BaseMainFragment

class JustAMinuteFragment : BaseMainFragment<IJustAMinuteView, IJustAMinutePresenter>(), IJustAMinuteView {
    override fun createPresenter(): IJustAMinutePresenter = JustAMinutePresenter(appComponent)

    override val resourceId: Int get() = R.layout.just_a_minute_fragment

    override val titleId: Int get() = R.string.just_a_minute

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindButterKnife(view)
    }
}