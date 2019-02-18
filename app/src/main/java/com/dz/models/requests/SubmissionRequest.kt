package com.dz.models.requests

import com.google.gson.annotations.SerializedName

data class SubmissionRequest(@SerializedName("description")
                             var description: String? = "",
                             @SerializedName("contestId")
                             var contestId: Int? = 0,
                             @SerializedName("friends")
                             var friends: ArrayList<Int?>? = null,
                             @SerializedName("images")
                             var images: ArrayList<String?>? = null,
                             @SerializedName("video")
                             var video: String? = "",
                             @SerializedName("videoThumb")
                             var videoThumb: String? = "")