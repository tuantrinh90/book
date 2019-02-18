package com.dz.ui.fragments.accounts.changepasswords

import android.os.Bundle
import android.view.View
import com.dz.commons.fragments.BaseMvpFragment
import com.dz.ui.R

class ChangePasswordFragment : BaseMvpFragment<IChangePasswordView, IChangePasswordPresenter>(), IChangePasswordView {
    override fun createPresenter(): IChangePasswordPresenter = ChangePasswordPresenter(appComponent)

    /**
     * @return id of layout use in fragment
     */
    override val resourceId: Int get() = R.layout.change_password_fragment

    override val titleId: Int get() = R.string.change_password

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindButterKnife(view)
    }
}