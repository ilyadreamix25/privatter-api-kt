package com.privatter.api.v2.common.model

import com.privatter.api.v2.common.PrivatterStatusCode
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.json.JsonObject

/** Type alias for Privatter response model with empty body. */
typealias PrivatterEmptyResponseModel = PrivatterResponseModel<JsonObject>

/**
 * Information about request processing.
 * @param code Response status code. See [PrivatterStatusCode].
 * @param name Response status name. Usually [PrivatterStatusCode.name].
 * @param timestamp Response timestamp.
 * @param data The result of the response, the data that the user requested.
 * @param hasError Indicates whether there was an error while processing the request.
 * @param error Information about the error, if any.
 */
@Serializable
data class PrivatterResponseModel<T>(
    @SerialName("code")
    val code: Int,

    @SerialName("name")
    val name: String,

    @SerialName("timestamp")
    val timestamp: Long = System.currentTimeMillis(),

    @SerialName("data")
    val data: T? = null,

    @SerialName("hasError")
    val hasError: Boolean = true,

    @SerialName("error")
    val error: PrivatterResponseModelError? = null
)
