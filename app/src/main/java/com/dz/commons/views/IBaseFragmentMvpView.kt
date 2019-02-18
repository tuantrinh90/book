package com.dz.commons.views

import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.ActionBar
import com.dz.models.responses.UploadResponse
import com.trello.rxlifecycle3.LifecycleProvider
import com.trello.rxlifecycle3.android.FragmentEvent

interface IBaseFragmentMvpView : IBaseMvpView, LifecycleProvider<FragmentEvent> {
    val titleId: Int

    val titleString: String

    @get:LayoutRes
    val resourceId: Int

    fun bindButterKnife(view: View)

    fun initToolbar(supportActionBar: ActionBar)

    fun showToastError(message: String?, dismissConsumer: (() -> Unit)? = null)

    fun showToastInfo(message: String?, dismissConsumer: (() -> Unit)? = null)

    fun showToastSuccess(message: String?, dismissConsumer: (() -> Unit)? = null)

    fun showToastWarm(message: String?, dismissConsumer: (() -> Unit)? = null)

    fun onUploadSuccess(response: UploadResponse?)
}