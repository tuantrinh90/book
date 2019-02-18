package com.dz.utilities

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupWindow
import androidx.fragment.app.Fragment
import com.dz.commons.activities.BaseAppCompatActivity
import com.dz.commons.activities.alonefragment.AloneFragmentActivity
import com.dz.customizes.views.popupviews.PopupView
import com.dz.libraries.utilities.*
import com.dz.libraries.views.keypairs.ExtKeyPair
import com.dz.libraries.views.keypairs.ExtKeyPairDialogFragment
import com.dz.ui.R
import com.dz.ui.fragments.fileviewers.FileViewerFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class AppUtility {
    companion object {
        fun getCalendarNoTime(): Calendar {
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            return calendar
        }

        fun getMinCalendar(): Calendar {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.YEAR, -120)
            return calendar
        }

        fun getMaxCalendar(): Calendar {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.YEAR, 120)
            return calendar
        }

        /**
         * ^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{6,}$ Minimum eight characters, at least one uppercase letter, one lowercase letter,
         * one number and one special character
         * ^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,10}$ Minimum eight and maximum 10 characters, at least one uppercase letter,
         * one lowercase letter, one number and one special character
         *
         * @param pass
         * @return
         */
        @Suppress("Annotator")
        fun isValidPass(pass: String): Boolean {
            return pass.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#%^*])[A-Za-z\\d@$!%*?&#%^*]{6,}$".toRegex())
        }

        fun getCameraOrGallery(context: Context, includeLibrary: Boolean): ArrayList<ExtKeyPair?>? {
            with(ArrayList<ExtKeyPair?>()) {
                add(ExtKeyPair(context.getString(com.dz.ui.R.string.camera), context.getString(com.dz.ui.R.string.camera)))
                add(ExtKeyPair(context.getString(com.dz.ui.R.string.gallery), context.getString(com.dz.ui.R.string.gallery)))
                if (includeLibrary) {
                    add(ExtKeyPair(context.getString(com.dz.ui.R.string.library), context.getString(com.dz.ui.R.string.library)))
                }
                return this
            }
        }

        /**
         * @param compositeDisposable
         * @param mActivity
         * @param fragment
         * @param filePath MediaUtility.getImageUrlPng();
         */
        fun actionImageCameraOrGallery(compositeDisposable: CompositeDisposable, mActivity: BaseAppCompatActivity<*, *>,
                                       fragment: Fragment, filePath: File) {
            compositeDisposable.add(mActivity.rxPermissions
                    .request(Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : DisposableObserver<Boolean>() {
                        override fun onComplete() {}
                        override fun onNext(granted: Boolean) {
                            if (!granted) return
                            ExtKeyPairDialogFragment.newInstance()
                                    .setExtKeyValuePairs(getCameraOrGallery(mActivity, false))
                                    .setOnSelectedConsumer {
                                        if (it.key.equals(mActivity.getString(com.dz.ui.R.string.camera), true)) {
                                            MediaUtility.captureImage(fragment, filePath)
                                        }

                                        if (it.key.equals(mActivity.getString(com.dz.ui.R.string.gallery), true)) {
                                            MediaUtility.chooseImageFromGallery(fragment, mActivity.getString(com.dz.ui.R.string.select_value))
                                        }
                                    }
                                    .show(fragment.fragmentManager, null)
                        }

                        override fun onError(e: Throwable) {}
                    }))
        }

        /**
         * @param mActivity
         * @param requestCode
         * @param resultCode
         * @param data
         * @param filePath
         * @param ivImage
         * @param fileConsumer(filePath)
         */
        fun actionImageCameraOrGalleryOnActivityResult(mActivity: Activity, requestCode: Int, resultCode: Int,
                                                       data: Intent?, filePath: File?, ivImage: ImageView?,
                                                       fileConsumer: ((File) -> Unit)? = null) {
            if (resultCode != Activity.RESULT_OK) return

            when (requestCode) {
                MediaUtility.CAMERA_IMAGE_REQUEST -> {
                    // display image
                    OptionalUtility.with(ivImage).doIfPresent {
                        PicasoUtility.get()
                                .load(filePath!!)
                                .placeholder(R.drawable.bg_background_radius_component)
                                .into(it)
                    }
                    // consumer file
                    OptionalUtility.with(fileConsumer).doIfPresent { it(filePath!!) }
                }
                MediaUtility.REQUEST_PICK_IMAGE -> {
                    val pathFile = MediaUtility.getPath(mActivity, data?.data)
                    if (StringUtility.isNullOrEmpty(pathFile)) return

                    FileUtility.with(FileUtility.getFileFromPath(pathFile)).doIfPresenter { f ->
                        // display image
                        OptionalUtility.with(ivImage).doIfPresent {
                            PicasoUtility.get()
                                    .load(f)
                                    .placeholder(R.drawable.bg_background_radius_component)
                                    .into(it)
                        }

                        // consumer file
                        OptionalUtility.with(fileConsumer).doIfPresent { c -> c(f) }
                    }
                }
            }
        }

        /**
         * @param compositeDisposable
         * @param mActivity
         * @param fragment
         * @param filePath MediaUtility.getVideoNameMp4();
         */
        fun actionVideoCameraOrGallery(compositeDisposable: CompositeDisposable, mActivity: BaseAppCompatActivity<*, *>,
                                       fragment: Fragment, filePath: File, consumerLibray: (() -> Unit)? = null) {
            compositeDisposable.add(mActivity.rxPermissions
                    .request(Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : DisposableObserver<Boolean>() {
                        override fun onComplete() {}
                        override fun onNext(granted: Boolean) {
                            if (!granted) return
                            ExtKeyPairDialogFragment.newInstance()
                                    .setTitle(mActivity.getResources().getString(com.dz.ui.R.string.upload_video))
                                    .setExtKeyValuePairs(getCameraOrGallery(mActivity, true))
                                    .setOnSelectedConsumer {
                                        if (it.key.equals(mActivity.getString(com.dz.ui.R.string.camera), true)) {
                                            MediaUtility.captureVideo(fragment, filePath, Constant.VIDEO_DURATION)
                                        }

                                        if (it.key.equals(mActivity.getString(com.dz.ui.R.string.gallery), true)) {
                                            MediaUtility.chooseVideoFromGallery(fragment, mActivity.getString(com.dz.ui.R.string.select_value))
                                        }

                                        if (it.key.equals(mActivity.getString(com.dz.ui.R.string.library), true)) {
                                            consumerLibray?.invoke()
                                        }
                                    }
                                    .show(fragment.fragmentManager, null)
                        }

                        override fun onError(e: Throwable) {}
                    }))
        }

        /**
         * @param mActivity
         * @param requestCode
         * @param resultCode
         * @param data
         * @param filePath
         * @param fileConsumer(filePath)
         */
        fun actionVideoCameraOrGalleryOnActivityResult(mActivity: Activity, requestCode: Int, resultCode: Int,
                                                       data: Intent?, filePath: File?, fileConsumer: ((File) -> Unit)? = null) {
            if (resultCode != Activity.RESULT_OK) return

            when (requestCode) {
                MediaUtility.CAMERA_VIDEO_REQUEST -> {
                    OptionalUtility.with(fileConsumer).doIfPresent { it(filePath!!) }
                }
                MediaUtility.REQUEST_PICK_VIDEO -> {
                    val pathFile = MediaUtility.getPath(mActivity, data?.data)
                    if (StringUtility.isNullOrEmpty(pathFile)) return

                    FileUtility.with(FileUtility.getFileFromPath(pathFile)).doIfPresenter { f ->
                        OptionalUtility.with(fileConsumer).doIfPresent { c -> c(f) }
                    }
                }
            }
        }

        fun getColorWithAlpha(color: Int, ratio: Float): Int {
            val r = Color.red(color)
            val g = Color.green(color)
            val b = Color.blue(color)
            return Color.argb(Math.round(Color.alpha(color) * (if (ratio > 1) 1f else ratio)), r, g, b)
        }

        @Suppress("UNCHECKED_CAST")
        fun <T : Number> getNumberDivision(number: T): T {
            return when (number) {
                is Int -> if (number <= 0) 1 as T else number
                is Long -> if (number <= 0) 1L as T else number
                is Float -> if (number <= 0) 1F as T else number
                is Double -> if (number <= 0) 1.0 as T else number
                else -> number
            }
        }

        // show pop up window
        var popUpWindow: PopupWindow? = null

        /**
         * @param mActivity
         * @param view
         * @param its
         */
        fun showPopUpWindow(mActivity: Activity, view: View, value: ExtKeyPair? = null, its: ArrayList<ExtKeyPair?>? = null,
                            consumer: ((ExtKeyPair?) -> Unit)? = null) {
            popUpWindow?.dismiss()
            popUpWindow = PopupWindow(PopupView(mActivity).setData(value, its) {
                popUpWindow?.dismiss()
                consumer?.invoke(it)
            }, view.measuredWidth, ViewGroup.LayoutParams.WRAP_CONTENT)
            popUpWindow?.setOnDismissListener { KeyboardUtility.hideSoftKeyboard(mActivity) }
            popUpWindow?.elevation = 5.0f
            popUpWindow?.isOutsideTouchable = true
            popUpWindow?.isFocusable = true
            popUpWindow?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            popUpWindow?.showAsDropDown(view)
        }

        fun getSortByTime(context: Context): ArrayList<ExtKeyPair?> {
            val results = ArrayList<ExtKeyPair?>()
            results.add(ExtKeyPair(Constant.SORT_NEWEST, context.getString(com.dz.ui.R.string.newest_to_oldest)))
            results.add(ExtKeyPair(Constant.SORT_OLDEST, context.getString(com.dz.ui.R.string.oldest_to_newest)))
            return results
        }

        fun getSortByStatus(context: Context): ArrayList<ExtKeyPair?> {
            val results = ArrayList<ExtKeyPair?>()
            results.add(ExtKeyPair(Constant.FILTER_JOINED, context.getString(com.dz.ui.R.string.joined)))
            results.add(ExtKeyPair(Constant.FILTER_NOT_JOINED_YET, context.getString(com.dz.ui.R.string.not_joined_yet)))
            results.add(ExtKeyPair(Constant.FILTER_ALL, context.getString(com.dz.ui.R.string.all)))
            return results
        }

        fun getFileType(file: File): String {
            return when {
                MediaUtility.isImage(file) -> Constant.UPLOAD_TYPE_FILES
                MediaUtility.isVideo(file) -> Constant.UPLOAD_TYPE_VIDEOS_60
                else -> Constant.UPLOAD_TYPE_FILES
            }
        }

        fun viewFile(context: Context, path: String?) {
            if (!NetworkUtility.isNetworkAvailable(context)) return

            StringUtility.with(path)
                    .doIfPresent {
                        AloneFragmentActivity.with(context)
                                .parameters(FileViewerFragment.newBundle(it))
                                .start(FileViewerFragment::class.java)
                    }
        }

        fun viewFile(fragment: Fragment, path: String?) = viewFile(fragment.context!!, path)
    }
}