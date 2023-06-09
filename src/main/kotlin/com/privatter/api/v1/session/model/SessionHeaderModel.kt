package com.privatter.api.v1.session.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SessionHeaderModel(
    @SerialName("algorithm")
    val algorithm: String,

    @SerialName("version")
    val version: String
)
