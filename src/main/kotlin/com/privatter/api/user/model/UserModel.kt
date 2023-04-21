package com.privatter.api.user.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserModel(
    @SerialName("id")
    val id: String,

    @SerialName("profile")
    val profile: UserModelProfile,

    @SerialName("metadata")
    val metadata: UserModelMetadata = UserModelMetadata()
)
