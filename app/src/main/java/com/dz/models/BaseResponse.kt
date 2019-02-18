package com.dz.models

import com.dz.libraries.models.IModel
import com.google.gson.annotations.SerializedName

class BaseResponse<T>(@SerializedName("data")
                      val data: T? = null,
                      @SerializedName("message")
                      val message: String? = null,
                      @SerializedName("success")
                      val success: Boolean = false) : IModel