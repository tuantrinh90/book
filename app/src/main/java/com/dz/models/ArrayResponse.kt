package com.dz.models

import com.dz.libraries.models.IModel
import com.google.gson.annotations.SerializedName

data class ArrayResponse<T>(@SerializedName("total")
                            var total: Int? = 0,
                            @SerializedName("items")
                            var items: ArrayList<T?>? = null) : IModel