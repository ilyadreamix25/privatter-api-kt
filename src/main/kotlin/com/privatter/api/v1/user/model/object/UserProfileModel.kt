package com.privatter.api.v1.user.model.`object`

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserProfileModel(
    @SerialName("nickname")
    val nickname: String,

    @SerialName("description")
    val description: String? = null,

    @SerialName("iconUrl")
    val iconUrl: String? = null,
)
