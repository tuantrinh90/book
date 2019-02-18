package com.dz.ui.fragments.accounts.signins

import android.os.Bundle
import android.view.View
import butterknife.BindView
import butterknife.OnClick
import com.dz.commons.fragments.BaseMvpFragment
import com.dz.customizes.views.edittexts.EditTextApp
import com.dz.libraries.utilities.ActivityUtility
import com.dz.libraries.utilities.StringUtility
import com.dz.libraries.utilities.TextUtility
import com.dz.libraries.views.textviews.ExtTextView
import com.dz.ui.BuildConfig
import com.dz.ui.R
import com.dz.ui.activities.main.MainActivity

class SignInFragment : BaseMvpFragment<ISignInView, ISignInPresenter>(), ISignInView {
    @BindView(R.id.edtUserName)
    lateinit var edtUserName: EditTextApp
    @BindView(R.id.edtPass)
    lateinit var edtPass: EditTextApp
    @BindView(R.id.tvForgotEmail)
    lateinit var tvForgotEmail: ExtTextView
    @BindView(R.id.tvForgotPassword)
    lateinit var tvForgotPassword: ExtTextView

    override fun createPresenter(): ISignInPresenter = SignInPresenter(appComponent)

    override val resourceId: Int get() = R.layout.sign_in_fragment

    override val titleId: Int get() = R.string.sign_in

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindButterKnife(view)
        initViews()
    }

    override fun onSuccess() {
        // start main
        ActivityUtility.startActivity(mActivity, MainActivity::class.java)
        mActivity.finish()
    }

    @Suppress("DEPRECATION")
    fun initViews() {
        // show clear text
        edtUserName.textChangeConsumer = {
            edtUserName.iconRightImageView.visibility =
                    if (StringUtility.isNullOrEmpty(it)) View.INVISIBLE else View.VISIBLE
        }
        edtUserName.iconRightImageView.setOnClickListener { edtUserName.setContent("") }

        // underline
        TextUtility.underLine(tvForgotEmail)
        TextUtility.underLine(tvForgotPassword)

        // show password
        edtPass.onShowOrHidePassword()

        // debug mode
        if (BuildConfig.DEBUG) {
            edtUserName.setContent("huonglm@qsoftvietnam.com")
            edtPass.setContent("123456789")
        }
    }

    fun isValid(): Boolean {
        if (edtUserName.isEmpty(mActivity) {
                    edtUserName.etContent.requestFocus()
                    showToastError(it)
                }) return false

        if (edtPass.isEmpty(mActivity) {
                    edtPass.etContent.requestFocus()
                    showToastError(it)
                }) return false

        return true
    }

    @OnClick(R.id.tvForgotEmail)
    fun onClickForgotEmail() {

    }

    @OnClick(R.id.tvForgotPassword)
    fun onClickForgotPassword() {

    }

    @OnClick(R.id.btnLogin)
    fun onClickLogin() {
        if (!isValid()) return

        presenter.login(edtUserName.getContent(), edtPass.getContent())
    }

    @OnClick(R.id.llSignUp)
    fun onClickSignUp() {

    }

    @OnClick(R.id.ivFacebook)
    fun onClickFacebook() {

    }

    @OnClick(R.id.ivGoogle)
    fun onClickGoogle() {

    }

    @OnClick(R.id.ivKakaoTalk)
    fun onClickKakaoTalk() {

    }

    @OnClick(R.id.ivTelegram)
    fun onClickTelegram() {

    }
}