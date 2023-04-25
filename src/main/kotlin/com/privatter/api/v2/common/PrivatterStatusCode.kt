package com.privatter.api.v2.common

import com.privatter.api.v2.common.model.PrivatterEmptyResponseModel
import com.privatter.api.v2.common.model.PrivatterResponseModelError
import com.privatter.api.v2.common.model.PrivatterResponseModel

/** Default Privatter response status codes. */
enum class PrivatterStatusCode(val privatterCode: Int, val httpCode: Int = 400) {
    COMMON_OK(0, 200),
    COMMON_INVALID_REQUEST(1000),
    COMMON_INVALID_METHOD(1001),
    COMMON_UNKNOWN_SERVICE(1002, 404),
    COMMON_INTERNAL_SERVER_ERROR(1003, 500),
    COMMON_TOO_MANY_REQUESTS(1004, 429),
    COMMON_INVALID_SIGNATURE(1005),

    USER_ALREADY_EXISTS(2000),
    USER_DOES_NOT_EXIST(2001),
    USER_VERIFICATION_REQUIRED(2002),
    USER_INVALID_VERIFICATION_TOKEN(2003),
    USER_ACTIVATE_ACCOUNT(2004),
    USER_INVALID_PASSWORD(2005),
    USER_DISABLED(2006, 403);


    val errorModel: PrivatterEmptyResponseModel = this.parseModel(hasError = false)
    val okModel: PrivatterEmptyResponseModel = this.parseModel()

    fun <T> PrivatterStatusCode.parseModel(
        data: T? = null,
        hasError: Boolean = true,
        error: PrivatterResponseModelError? = null
    ) = PrivatterResponseModel(
        code = this.privatterCode,
        name = this.name,
        data = data,
        hasError = hasError,
        error = error
    )
}
