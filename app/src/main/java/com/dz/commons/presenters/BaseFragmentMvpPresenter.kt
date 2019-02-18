package com.dz.commons.presenters

import android.util.Log
import com.dz.commons.views.IBaseFragmentMvpView
import com.dz.di.AppComponent
import com.dz.libraries.utilities.MediaUtility
import com.dz.listeners.ApiCallback
import com.dz.models.responses.UploadResponse
import com.dz.utilities.AppUtility
import com.dz.utilities.RxUtility
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

abstract class BaseFragmentMvpPresenter<V : IBaseFragmentMvpView>(
        appComponent: AppComponent
) : BaseMvpPresenter<V>(appComponent), IBaseFragmentMvpPresenter<V> {
    override fun uploadFile(file: File) {
        getView {
            val body = MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", file.name, RequestBody
                            .create(MediaType.parse(when {
                                MediaUtility.isImage(file) -> "image/${MediaUtility.getImageExtension(file)}"
                                MediaUtility.isVideo(file) -> "video/${MediaUtility.getVideoExtension(file)}"
                                else -> "*/*"
                            }), file))
                    .build()

            RxUtility.async(it, dataModule.fileApiService.uploadVideo(AppUtility.getFileType(file), body),
                    object : ApiCallback<UploadResponse>() {
                        override fun onStart() {
                            it.showLoading(true)
                        }

                        override fun onFinish() {
                            it.showLoading(false)
                        }

                        override fun onSuccess(t: UploadResponse?) {
                            it.onUploadSuccess(t)
                        }

                        override fun onError(error: String?) {
                            it.showToastError(error)
                        }
                    })
        }
    }
}