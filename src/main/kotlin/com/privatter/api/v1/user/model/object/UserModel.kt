package com.privatter.api.v1.user.model.`object`

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserModel(
    @SerialName("id")
    val id: String,

    @SerialName("profile")
    val profile: UserProfileModel,

    @SerialName("metadata")
    val metadata: UserMetadataModel = UserMetadataModel()
)
