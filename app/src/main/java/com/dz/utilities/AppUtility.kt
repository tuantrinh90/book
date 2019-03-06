package com.dz.utilities

import android.Manifest
import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Parcelable
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.dz.commons.activities.BaseAppCompatActivity
import com.dz.customizes.views.popupviews.PopupView
import com.dz.libraries.utilities.*
import com.dz.libraries.views.keypairs.ExtKeyPair
import com.dz.libraries.views.keypairs.ExtKeyPairDialogFragment
import com.dz.ui.R
import com.google.android.gms.plus.PlusShare
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

        fun shareSocial(activity: Activity?) {
            val targetShareIntents = java.util.ArrayList<Intent>()
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.type = "text/plain"
            val resInfos = activity!!.packageManager.queryIntentActivities(shareIntent, 0)
            if (!resInfos.isEmpty()) {
                for (resInfo in resInfos) {
                    val packageName = resInfo.activityInfo.packageName
                    Log.d("packageName", "shareSocial: $packageName")
                    if (packageName.contains("com.facebook.katana")) {
                        val intent = Intent()
                        intent.component = ComponentName(packageName, resInfo.activityInfo.name)
                        intent.action = Intent.ACTION_SEND
                        intent.type = "text/plain"
                        intent.putExtra(Intent.EXTRA_TEXT, Constant.APP_LINK_SHARE)
                        intent.setPackage(packageName)
                        intent.setClassName(resInfo.activityInfo.packageName, resInfo.activityInfo.name)
                        targetShareIntents.add(intent)
                    }
                    if (packageName.contains("com.samsung.android.messaging")) {
                        val intent = Intent()
                        intent.component = ComponentName(packageName, resInfo.activityInfo.name)
                        intent.action = Intent.ACTION_SEND
                        intent.type = "text/plain"
                        intent.putExtra(Intent.EXTRA_TEXT, Constant.APP_LINK_SHARE)
                        intent.setPackage(packageName)
                        targetShareIntents.add(intent)
                    }
                    if (packageName.contains("com.twitter.android")) {
                        val intent = Intent()
                        intent.component = ComponentName(packageName, resInfo.activityInfo.name)
                        intent.action = Intent.ACTION_SEND
                        intent.type = "text/plain"
                        intent.putExtra(Intent.EXTRA_TEXT, Constant.APP_LINK_SHARE)
                        intent.setPackage(packageName)
                        targetShareIntents.add(intent)
                    }
                    if (packageName.contains("com.instagram.android")) {
                        val intent = Intent()
                        intent.component = ComponentName(packageName, resInfo.activityInfo.name)
                        intent.action = Intent.ACTION_SEND
                        intent.type = "image/*"
                        intent.putExtra(Intent.EXTRA_TEXT, Constant.APP_LINK_SHARE)
                        intent.setPackage(packageName)
                        targetShareIntents.add(intent)
                    }
                    if (packageName.contains("com.samsung.android.email.provider")) {
                        val intent = Intent()
                        intent.component = ComponentName(packageName, resInfo.activityInfo.name)
                        intent.action = Intent.ACTION_SEND
                        intent.type = "text/plain"
                        intent.putExtra(Intent.EXTRA_TEXT, Constant.APP_LINK_SHARE)
                        intent.setPackage(packageName)
                        targetShareIntents.add(intent)
                    }
                    if (packageName.contains("com.google.android.gm")) {
                        val intent = PlusShare.Builder(activity)
                                .setType("text/plain")
                                .setText("Welcome to Ebook")
                                .setContentUrl(Uri.parse(Constant.APP_LINK_SHARE))
                                .intent
                        targetShareIntents.add(intent)
                    }
                }
                if (!targetShareIntents.isEmpty()) {
                    val chooserIntent = Intent.createChooser(targetShareIntents.removeAt(0), "Select application to share!")
                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetShareIntents.toTypedArray<Parcelable>())
                    activity.startActivityForResult(chooserIntent, 200)
                } else {
                    Toast.makeText(activity, activity!!.resources.getString(R.string.no_app_share), Toast.LENGTH_SHORT).show()
                }
            }

        }

    }
}