package com.dz.commons.fragments.toast

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import butterknife.BindView
import com.dz.commons.fragments.BaseMvpDialogFragment
import com.dz.libraries.utilities.BarUtility
import com.dz.libraries.utilities.StringUtility
import com.dz.libraries.views.textviews.ExtTextView
import com.dz.ui.R
import com.dz.utilities.Constant
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

class ToastDialogFragment : BaseMvpDialogFragment<IToastView, IToastPresenter>() {
    companion object {
        fun showMessage(fragmentManager: FragmentManager?, toastType: ToastType, message: String?, dismissConsumer: (() -> Unit)? = null) {
            if (StringUtility.isNullOrEmpty(message)) return

            ToastDialogFragment()
                    .setToastType(toastType)
                    .setMessage(message)
                    .setDismissConsumer(dismissConsumer)
                    .show(fragmentManager, null)
        }
    }

    enum class ToastType {
        SUCCESS,
        ERROR,
        WARN,
        INFO
    }

    @BindView(R.id.vMessage)
    lateinit var vMessage: LinearLayout
    @BindView(R.id.ivIcon)
    lateinit var ivIcon: ImageView
    @BindView(com.dz.ui.R.id.tvMessage)
    lateinit var tvMessage: ExtTextView

    var mMessage: String? = null
    var mToastType: ToastType = ToastType.SUCCESS
    var mDismissConsumer: (() -> Unit)? = null

    override fun createPresenter(): IToastPresenter = ToastPresenter(appComponent)

    override val resourceId: Int get() = com.dz.ui.R.layout.toast_fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, com.dz.ui.R.style.AppDialogTheme)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindButterKnife(view)
        configs()
        initViews()
    }

    override fun onStop() {
        mDismissConsumer?.invoke()
        super.onStop()
    }

    fun initViews() {
        // padding view
        val padding = resources.getDimensionPixelSize(R.dimen.padding_layout)
        val paddingTop = BarUtility.getStatusBarHeight(mActivity)
        vMessage.setPadding(padding, paddingTop, padding, padding)

        // set color
        vMessage.setBackgroundResource(when (mToastType) {
            ToastType.SUCCESS -> R.color.color_success
            ToastType.ERROR -> R.color.color_error
            ToastType.WARN -> R.color.color_warn
            ToastType.INFO -> R.color.color_info
        })

        // set icon
        ivIcon.setImageResource(when (mToastType) {
            ToastType.SUCCESS -> R.drawable.ic_success
            ToastType.ERROR -> R.drawable.ic_error
            ToastType.WARN -> R.drawable.ic_warn
            ToastType.INFO -> R.drawable.ic_info
        })

        // display message
        tvMessage.text = mMessage

        // auto dissmiss
        subscribeWith {
            Observable.timer(Constant.TOAST_TIME, TimeUnit.MILLISECONDS)
                    .subscribe { dismiss() }
        }
    }

    fun configs() {
        dialog?.window?.apply {
            // hide status bar
            // view.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            //  or View.SYSTEM_UI_FLAG_FULLSCREEN)

            // background
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            // height dialog
            val heightLayout = BarUtility.getStatusBarHeight(mActivity) + BarUtility.getActionBarHeight(mActivity)
            setLayout(WindowManager.LayoutParams.MATCH_PARENT, heightLayout)

            // display dialog at top screen
            setGravity(Gravity.TOP)
        }
    }

    fun setToastType(toastType: ToastType): ToastDialogFragment {
        mToastType = toastType
        return this
    }

    fun setMessage(message: String?): ToastDialogFragment {
        mMessage = message
        return this
    }

    fun setDismissConsumer(dismissConsumer: (() -> Unit)? = null): ToastDialogFragment {
        mDismissConsumer = dismissConsumer
        return this
    }
}