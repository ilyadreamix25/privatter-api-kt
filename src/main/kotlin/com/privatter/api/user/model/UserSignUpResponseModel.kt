package com.privatter.api.user.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserSignUpResponseModel(
    @SerialName("user")
    val user: UserModel,

    @SerialName("userSessionToken")
    val sessionToken: String
)
