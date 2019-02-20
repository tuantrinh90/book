package com.dz.models.responses

import com.dz.libraries.models.IModel
import com.google.gson.annotations.SerializedName

data class BookResponse(@SerializedName("id")
                        var id: Int? = 0,
                        @SerializedName("link")
                        var link: String? = "",
                        @SerializedName("name")
                        var name: String? = "",
                        @SerializedName("author")
                        var author: String? = "",
                        @SerializedName("category")
                        var categoryId: Int? = 0) : IModel