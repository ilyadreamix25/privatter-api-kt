package com.privatter.api.v1.user.model

import com.privatter.api.v1.user.model.`object`.UserModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserSignInResponseModel(
    @SerialName("user")
    val user: UserModel,

    @SerialName("userSessionToken")
    val userSessionToken: String
)
