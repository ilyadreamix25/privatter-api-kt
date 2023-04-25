package com.privatter.api.v1.user.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserRequestActionVerificationResponse(
    @SerialName("verificationTokenId")
    val verificationTokenId: String
)
