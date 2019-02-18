package com.dz.models.responses

import com.dz.libraries.models.IModel
import com.google.gson.annotations.SerializedName

data class SignInResponse(@SerializedName("accessToken")
                          var accessToken: String? = "",
                          @SerializedName("refreshToken")
                          var refreshToken: String? = "",
                          @SerializedName("refreshTokenExpiresAt")
                          var refreshTokenExpiresAt: String? = "",
                          @SerializedName("member_id")
                          var memberId: Int? = 0,
                          @SerializedName("member")
                          var member: MemberResponse? = null) : IModel