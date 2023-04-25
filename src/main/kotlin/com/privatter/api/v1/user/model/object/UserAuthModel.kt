package com.privatter.api.v1.user.model.`object`

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserAuthModel(
    @SerialName("key")
    val key: String,

    @SerialName("value")
    val value: String
)
