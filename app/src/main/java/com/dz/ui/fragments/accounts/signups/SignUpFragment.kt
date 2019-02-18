package com.dz.ui.fragments.accounts.signups

import android.os.Bundle
import android.view.View
import com.dz.commons.fragments.BaseMvpFragment
import com.dz.ui.R

class SignUpFragment : BaseMvpFragment<ISignUpView, ISignUpPresenter>(), ISignUpView {
    override fun createPresenter(): ISignUpPresenter = SignUpPresenter(appComponent)

    /**
     * @return id of layout use in fragment
     */
    override val resourceId: Int get() = R.layout.sign_up_fragment

    override val titleId: Int get() = R.string.sign_up

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindButterKnife(view)
    }
}