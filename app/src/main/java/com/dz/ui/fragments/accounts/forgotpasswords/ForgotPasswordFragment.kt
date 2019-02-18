package com.dz.ui.fragments.accounts.forgotpasswords

import android.os.Bundle
import android.view.View
import com.dz.commons.fragments.BaseMvpFragment
import com.dz.ui.R

class ForgotPasswordFragment : BaseMvpFragment<IForgotPasswordView, IForgotPasswordPresenter>(), IForgotPasswordView {
    override fun createPresenter(): IForgotPasswordPresenter = ForgotPasswordPresenter(appComponent)

    /**
     * @return id of layout use in fragment
     */
    override val resourceId: Int get() = R.layout.forgot_password_fragment

    override val titleId: Int get() = R.string.forgot_password

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindButterKnife(view)
    }
}