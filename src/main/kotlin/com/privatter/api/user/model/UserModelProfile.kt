package com.privatter.api.user.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserModelProfile(
    @SerialName("nickname")
    val nickname: String,

    @SerialName("description")
    val description: String? = null,

    @SerialName("iconUrl")
    val iconUrl: String? = null
)
