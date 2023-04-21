package com.privatter.api.user.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserModelMetadata(
    @SerialName("signedUpAt")
    val signedUpAt: Long = System.currentTimeMillis(),

    @SerialName("lastSignedInAt")
    val lastSignedInAt: Long = 0
)
