package com.privatter.api.v1.user.model.`object`

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserMetadataModel(
    @SerialName("signedUpAt")
    val signedUpAt: Long = System.currentTimeMillis(),

    @SerialName("lastSignedInAt")
    val lastSignedInAt: Long = 0
)
