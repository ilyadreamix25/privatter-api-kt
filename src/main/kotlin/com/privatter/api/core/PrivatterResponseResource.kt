package com.privatter.api.core

import com.privatter.api.core.model.PrivatterEmptyResponseModel
import com.privatter.api.core.model.PrivatterResponseModel

private const val UPDATE_APP = "Update the app to the latest version."
private const val TRY_AGAIN_LATER = "Please try again later."

enum class PrivatterResponseResource(val statusCode: Int, val message: String) {
    OK(0, "OK."),

    INVALID_REQUEST(1000, "Invalid request. $UPDATE_APP"),
    INVALID_SERVICE(1001, "Invalid service. $UPDATE_APP"),
    INVALID_METHOD(1002, "Invalid method. $UPDATE_APP"),
    SERVER_ERROR(1003, "Server error. $TRY_AGAIN_LATER"),

    USER_EXISTS(2000, USER_EXISTS.name),
    USER_VERIFICATION_REQUIRED(2001, USER_VERIFICATION_REQUIRED.name);

    companion object {
        fun <T> parseOk(data: T? = null) = PrivatterResponseModel(
            message = OK.message,
            statusCode = OK.statusCode,
            data = data,
            hasError = false,
            errorMessage = null,
            errorStackTrace = null,
        )

        fun parseError(
            resource: PrivatterResponseResource,
            errorMessage: String? = null,
            errorStackTrace: List<String>? = null
        ) = PrivatterEmptyResponseModel(
            message = resource.message,
            statusCode = resource.statusCode,
            errorMessage = errorMessage,
            errorStackTrace = errorStackTrace
        )
    }
}
