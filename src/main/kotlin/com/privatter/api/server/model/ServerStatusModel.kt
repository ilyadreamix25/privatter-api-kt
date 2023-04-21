package com.privatter.api.server.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ServerStatusModel(
    @SerialName("version")
    val version: String,

    @SerialName("debug")
    val debug: Boolean
)
