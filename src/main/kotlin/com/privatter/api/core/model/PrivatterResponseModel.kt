package com.privatter.api.core.model

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

    @SerialName("errorMessage")
    val errorMessage: String? = null,

    @SerialName("errorInformation")
    val errorInformation: List<String>? = null
)
