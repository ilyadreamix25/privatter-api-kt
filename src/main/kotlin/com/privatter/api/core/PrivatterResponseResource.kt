package com.privatter.api.core

import com.privatter.api.core.model.PrivatterEmptyResponseModel
import com.privatter.api.core.model.PrivatterResponseModel
import kotlinx.serialization.json.JsonObject

private const val UPDATE_APP = "Update the app to the latest version."
private const val TRY_AGAIN_LATER = "Please try again later."

enum class PrivatterResponseResource(val statusCode: Int, val message: String) {
    OK(0, "OK."),

    INVALID_REQUEST(1000, "Invalid request. $UPDATE_APP"),
    INVALID_SERVICE(1001, "Invalid service. $UPDATE_APP"),
    INVALID_METHOD(1002, "Invalid method. $UPDATE_APP"),
    SERVER_ERROR(1003, "Server error. $TRY_AGAIN_LATER"),

    USER_EXISTS(2000, "A user with such credentials already exists."),
    USER_VERIFICATION_REQUIRED(2001, "Verification required. Please check your mailbox.");

    object Model {
        val OK = parseOk<JsonObject>(null)
        val INVALID_REQUEST = parseError(PrivatterResponseResource.INVALID_REQUEST)
        val INVALID_SERVICE = parseError(PrivatterResponseResource.INVALID_SERVICE)
        val INVALID_METHOD = parseError(PrivatterResponseResource.INVALID_METHOD)
        val SERVER_ERROR = parseError(PrivatterResponseResource.SERVER_ERROR)
        val USER_EXISTS = parseError(PrivatterResponseResource.USER_EXISTS)
        val USER_VERIFICATION_REQUIRED = PrivatterEmptyResponseModel(
            message = PrivatterResponseResource.USER_VERIFICATION_REQUIRED.message,
            statusCode = PrivatterResponseResource.USER_VERIFICATION_REQUIRED.statusCode,
            hasError = false
        )
    }

    companion object {
        fun <T> parseOk(data: T? = null) = PrivatterResponseModel(
            message = OK.message,
            statusCode = OK.statusCode,
            data = data,
            hasError = false,
            errorMessage = null,
            errorInformation = null,
        )

        fun parseError(
            resource: PrivatterResponseResource,
            errorMessage: String? = null,
            errorInformation: List<String>? = null
        ) = PrivatterEmptyResponseModel(
            message = resource.message,
            statusCode = resource.statusCode,
            errorMessage = errorMessage,
            errorInformation = errorInformation
        )
    }
}
