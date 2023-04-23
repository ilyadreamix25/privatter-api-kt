package com.privatter.api.user.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserSignInResponseModel(
    @SerialName("user")
    val user: UserModel,

    @SerialName("userSessionToken")
    val userSessionToken: String
)
