package com.dz.ui.fragments.accounts.updateprofiles

import android.os.Bundle
import android.view.View
import com.dz.commons.fragments.BaseMvpFragment
import com.dz.ui.R

class UpdateProfileFragment : BaseMvpFragment<IUpdateProfileView, IUpdateProfilePresenter>(), IUpdateProfileView {
    override fun createPresenter(): IUpdateProfilePresenter = UpdateProfilePresenter(appComponent)

    /**
     * @return id of layout use in fragment
     */
    override val resourceId: Int get() = R.layout.update_profile_fragment

    override val titleId: Int get() = R.string.update_profile

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindButterKnife(view)
    }
}