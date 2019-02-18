package com.dz.models.responses

import com.dz.libraries.models.IModel
import com.google.gson.annotations.SerializedName

data class MemberResponse(@SerializedName("birthday")
                          var birthday: String? = null,
                          @SerializedName("lastName")
                          var lastName: String? = "",
                          @SerializedName("address")
                          var address: String? = null,
                          @SerializedName("gender")
                          var gender: String? = null,
                          @SerializedName("fullName")
                          var fullName: String? = null,
                          @SerializedName("photo")
                          var photo: String? = "",
                          @SerializedName("description")
                          var description: String? = null,
                          @SerializedName("countryId")
                          var countryId: String? = null,
                          @SerializedName("firstName")
                          var firstName: String? = "",
                          @SerializedName("createdAt")
                          var createdAt: String? = "",
                          @SerializedName("deletedAt")
                          var deletedAt: String? = null,
                          @SerializedName("provider")
                          var provider: String? = null,
                          @SerializedName("phone")
                          var phone: String? = null,
                          @SerializedName("id")
                          var id: Int? = 0,
                          @SerializedName("email")
                          var email: String? = "",
                          @SerializedName("status")
                          var status: Int? = 0,
                          @SerializedName("updatedAt")
                          var updatedAt: String? = "") : IModel {
    var isChecked: Boolean = false

    val fullNameText: String?
        get() = "$firstName $lastName"
}