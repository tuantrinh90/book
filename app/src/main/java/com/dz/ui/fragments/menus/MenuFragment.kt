package com.dz.ui.fragments.menus

import android.os.Bundle
import android.view.View
import com.dz.ui.R
import com.dz.ui.fragments.BaseMainFragment

class MenuFragment : BaseMainFragment<IMenuView, IMenuPresenter>(), IMenuView {
    override fun createPresenter(): IMenuPresenter = MenuPresenter(appComponent)

    override val resourceId: Int get() = R.layout.menu_fragment

    override val titleId: Int get() = R.string.menu

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindButterKnife(view)
    }
}