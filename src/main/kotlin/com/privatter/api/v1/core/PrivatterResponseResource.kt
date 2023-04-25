package com.privatter.api.v1.core

import com.privatter.api.v1.core.model.PrivatterEmptyResponseModel
import com.privatter.api.v1.core.model.PrivatterResponseModel
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

    USER_EXISTS(2000, "A user with such credentials already exists."),
    USER_VERIFICATION_REQUIRED(2001, "Verification required."),
    USER_DOES_NOT_EXIST(2002, "User does not exist."),
    USER_INVALID_PASSWORD(2003, "Invalid password."),
    USER_ACCOUNT_NOT_ACTIVATED(2004, "To sign in to your account, first activate it."),
    USER_VERIFICATION_INVALID_DATA(2005, "Invalid verification data. $TRY_AGAIN"),
    USER_SESSION_EXPIRED(2006, "Session expired. Please re-login.");

    val asErrorModel get() = parseError(this)

    companion object {
        val OK_MODEL = parseOk<JsonObject>()

        @JvmStatic
        fun <T> parseOk(data: T? = null) = PrivatterResponseModel(
            message = OK.message,
            statusCode = OK.statusCode,
            data = data,
            hasError = false,
            errorInformation = null
        )

        @JvmStatic
        fun parseError(
            resource: PrivatterResponseResource,
            errorInformation: PrivatterResponseModel.ErrorInformation? = null
        ) = PrivatterEmptyResponseModel(
            message = resource.message,
            statusCode = resource.statusCode,
            errorInformation = errorInformation
        )
    }
}
