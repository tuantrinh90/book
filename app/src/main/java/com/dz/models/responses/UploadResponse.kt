package com.dz.models.responses

import com.dz.libraries.models.IModel
import com.google.gson.annotations.SerializedName

data class UploadResponse(
        @SerializedName("path")
        var path: String? = "",
        @SerializedName("file")
        var file: String? = "",
        @SerializedName("thumbPath")
        var thumbPath: String? = "",
        @SerializedName("thumbFile")
        var thumbFile: String? = "",
        var position: Int = 0) : IModel