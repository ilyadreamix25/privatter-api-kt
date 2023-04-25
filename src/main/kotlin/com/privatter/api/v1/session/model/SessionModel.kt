package com.privatter.api.v1.session.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SessionModel(
    @SerialName("id")
    val id: String,

    @SerialName("userId")
    val userId: String
)
