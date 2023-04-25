package com.privatter.api.v2.common.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Information about the error during request processing.
 * @param message Exception message.
 * @param info Exception information. Usually stack trace.
 */
@Serializable
data class PrivatterResponseModelError(
    @SerialName("message")
    val message: String,

    @SerialName("info")
    val info: List<String> = listOf()
)
