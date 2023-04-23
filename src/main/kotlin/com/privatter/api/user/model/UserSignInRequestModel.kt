package com.privatter.api.user.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserSignInRequestModel(
    @SerialName("authKey")
    val authKey: String,

    @SerialName("authValue")
    val authValue: String,

    @SerialName("captchaToken")
    val captchaToken: String? = null,

    @SerialName("verificationTokenId")
    val verificationTokenId: String? = null,

    @SerialName("verificationTokenSecret")
    val verificationTokenSecret: String? = null,
)
