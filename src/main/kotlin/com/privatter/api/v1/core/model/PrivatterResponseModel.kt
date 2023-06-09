package com.privatter.api.v1.core.model

import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import org.springframework.http.ResponseEntity

typealias PrivatterEmptyResponseModel = PrivatterResponseModel<JsonObject>
typealias PrivatterResponseEntity <T> = ResponseEntity<PrivatterResponseModel<T>>
typealias PrivatterEmptyResponseEntity = ResponseEntity<PrivatterResponseModel<JsonObject>>

@Serializable
data class PrivatterResponseModel<T>(
    @SerialName("message")
    val message: String,

    @SerialName("statusCode")
    val statusCode: Int,

    @SerialName("data")
    val data: T? = null,

    @Required
    @SerialName("hasError")
    val hasError: Boolean = true,

    @SerialName("errorInformation")
    val errorInformation: ErrorInformation? = null
) {
    @Serializable
    data class ErrorInformation(
        @SerialName("message")
        val message: String? = null,

        @SerialName("stackTrace")
        val stackTrace: List<String>? = null
    )
}

fun <T> PrivatterEmptyResponseModel.cast(data: T? = null) = PrivatterResponseModel(
    message = this.message,
    statusCode = this.statusCode,
    data = data,
    hasError = this.hasError,
    errorInformation = this.errorInformation
)
