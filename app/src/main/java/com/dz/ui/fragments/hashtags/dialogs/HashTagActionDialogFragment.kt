package com.dz.ui.fragments.hashtags.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import butterknife.BindView
import butterknife.OnClick
import com.dz.commons.fragments.BaseMvpDialogFragment
import com.dz.libraries.utilities.StringUtility
import com.dz.libraries.views.edittexts.ExtEditText
import com.dz.models.responses.HashTagResponse
import com.dz.ui.R

class HashTagActionDialogFragment : BaseMvpDialogFragment<IHashTagActionView, IHashTagActionPresenter>(), IHashTagActionView {
    @BindView(R.id.etName)
    lateinit var etName: ExtEditText

    var saveConsumer: ((ArrayList<HashTagResponse?>?) -> Unit)? = null

    override fun createPresenter(): IHashTagActionPresenter = HashTagActionPresenter(appComponent)

    override val resourceId: Int get() = R.layout.hash_tag_action_dialog_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindButterKnife(view)
        configs()
    }

    override fun onSuccess(response: ArrayList<HashTagResponse?>?) {
        saveConsumer?.invoke(response)
        dismiss()
    }

    @OnClick(R.id.tvCancel)
    fun onClickCancel() {
        dismiss()
    }

    @OnClick(R.id.tvConfirm)
    fun onClickConfirm() {
        if (StringUtility.isNullOrEmpty(etName.text.toString())) {
            showToastError(getString(R.string.error_field_must_not_be_empty))
            etName.requestFocus()
            return
        }

        presenter.createHashTag(etName.text.toString())
    }

    fun configs() {
        dialog?.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    fun setConsumer(consumer: ((ArrayList<HashTagResponse?>?) -> Unit)? = null): HashTagActionDialogFragment {
        saveConsumer = consumer
        return this
    }
}