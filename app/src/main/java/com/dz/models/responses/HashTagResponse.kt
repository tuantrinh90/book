package com.dz.models.responses

import com.dz.libraries.models.IModel
import com.google.gson.annotations.SerializedName

data class HashTagResponse(@SerializedName("id")
                           var id: Int? = 0,
                           @SerializedName("name")
                           var name: String? = "",
                           @SerializedName("status")
                           var status: Int? = 0) : IModel