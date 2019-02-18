package com.dz.ui.fragments.accounts.myprofiles

import android.os.Bundle
import android.view.View
import com.dz.commons.fragments.BaseMvpFragment
import com.dz.ui.R

class MyProfileFragment : BaseMvpFragment<IMyProfileView, IMyProfilePresenter>(), IMyProfileView {
    override fun createPresenter(): IMyProfilePresenter = MyProfilePresenter(appComponent)

    /**
     * @return id of layout use in fragment
     */
    override val resourceId: Int get() = R.layout.my_profile_fragment

    override val titleId: Int get() = R.string.my_profile

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindButterKnife(view)
    }
}