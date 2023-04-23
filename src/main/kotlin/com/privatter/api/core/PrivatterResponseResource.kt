package com.privatter.api.core

import com.privatter.api.core.model.PrivatterEmptyResponseModel
import com.privatter.api.core.model.PrivatterResponseModel
import kotlinx.serialization.json.JsonObject

private const val UPDATE_APP = "Update the app to the latest version."
private const val TRY_AGAIN = "Please try again."

enum class PrivatterResponseResource(val statusCode: Int, val message: String) {
    OK(0, "OK."),

    INVALID_REQUEST(1000, "Invalid request. $UPDATE_APP"),
    INVALID_SERVICE(1001, "Invalid service. $UPDATE_APP"),
    INVALID_METHOD(1002, "Invalid method. $UPDATE_APP"),
    SERVER_ERROR(1003, "Server error. $TRY_AGAIN"),
    INVALID_SESSION(1004, "Invalid session. $UPDATE_APP"),
    SESSION_EXPIRED(1005, "Session expired. Please re-login."),

    USER_EXISTS(2000, "A user with such credentials already exists."),
    USER_VERIFICATION_REQUIRED(2001, "Verification required. Please check your mailbox."),
    USER_DOES_NOT_EXIST(2002, "User does not exist."),
    USER_INVALID_PASSWORD(2003, "Invalid password."),
    USER_ACCOUNT_NOT_ACTIVATED(2004, "To sign in to your account, first activate it."),
    USER_VERIFICATION_INVALID_DATA(2005, "Invalid verification data. $TRY_AGAIN");

    object Model {
        val OK = parseOk<JsonObject>(null)

        val INVALID_REQUEST = parseError(PrivatterResponseResource.INVALID_REQUEST)
        val INVALID_SERVICE = parseError(PrivatterResponseResource.INVALID_SERVICE)
        val INVALID_METHOD = parseError(PrivatterResponseResource.INVALID_METHOD)
        val SERVER_ERROR = parseError(PrivatterResponseResource.SERVER_ERROR)
        val INVALID_SESSION = parseError(PrivatterResponseResource.INVALID_SESSION)
        val SESSION_EXPIRED = parseError(PrivatterResponseResource.SESSION_EXPIRED)

        val USER_EXISTS = parseError(PrivatterResponseResource.USER_EXISTS)
        val USER_VERIFICATION_REQUIRED = parseError(PrivatterResponseResource.USER_VERIFICATION_REQUIRED)
        val USER_DOES_NOT_EXIST = parseError(PrivatterResponseResource.USER_DOES_NOT_EXIST)
        val USER_INVALID_PASSWORD = parseError(PrivatterResponseResource.USER_INVALID_PASSWORD)
        val USER_ACCOUNT_NOT_ACTIVATED = parseError(PrivatterResponseResource.USER_ACCOUNT_NOT_ACTIVATED)
        val USER_VERIFICATION_INVALID_DATA = parseError(PrivatterResponseResource.USER_VERIFICATION_INVALID_DATA)
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
