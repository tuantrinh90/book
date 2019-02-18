package com.dz.interactors.services

import com.dz.models.BaseResponse
import com.dz.models.responses.UploadResponse
import com.dz.utilities.ServiceConfig
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface IFileApiService {
    @POST(ServiceConfig.UPLOAD_FILE)
    fun uploadVideo(@Path("type") type: String, @Body body: RequestBody) : Observable<BaseResponse<UploadResponse>>
}