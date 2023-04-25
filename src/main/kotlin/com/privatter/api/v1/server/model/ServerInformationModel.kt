package com.privatter.api.v1.server.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ServerInformationModel(
    @SerialName("version")
    val version: String,

    @SerialName("debug")
    val debug: Boolean
)
