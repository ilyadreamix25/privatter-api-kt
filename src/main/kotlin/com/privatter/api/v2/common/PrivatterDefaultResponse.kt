package com.privatter.api.v2.common

import com.privatter.api.v2.common.model.PrivatterEmptyResponseModel

/** Default Privatter response models. */
enum class PrivatterDefaultResponse(
    val model: PrivatterEmptyResponseModel,
    val httpStatusCode: Int
) {
    //
    // COMMON
    // 1000-1999
    //

    COMMON_OK(
        PrivatterStatusCode.COMMON_OK.okModel,
        PrivatterStatusCode.COMMON_OK.httpCode
    ),
    COMMON_INVALID_REQUEST(
        PrivatterStatusCode.COMMON_INVALID_REQUEST.errorModel,
        PrivatterStatusCode.COMMON_INVALID_REQUEST.httpCode
    ),
    COMMON_INVALID_METHOD(
        PrivatterStatusCode.COMMON_INVALID_METHOD.errorModel,
        PrivatterStatusCode.COMMON_INVALID_METHOD.httpCode
    ),
    COMMON_UNKNOWN_SERVICE(
        PrivatterStatusCode.COMMON_UNKNOWN_SERVICE.errorModel,
        PrivatterStatusCode.COMMON_UNKNOWN_SERVICE.httpCode
    ),
    COMMON_INTERNAL_SERVER_ERROR(
        PrivatterStatusCode.COMMON_INTERNAL_SERVER_ERROR.errorModel,
        PrivatterStatusCode.COMMON_INTERNAL_SERVER_ERROR.httpCode
    ),
    COMMON_TOO_MANY_REQUESTS(
        PrivatterStatusCode.COMMON_TOO_MANY_REQUESTS.errorModel,
        PrivatterStatusCode.COMMON_TOO_MANY_REQUESTS.httpCode
    ),
    COMMON_INVALID_SIGNATURE(
        PrivatterStatusCode.COMMON_INVALID_SIGNATURE.errorModel,
        PrivatterStatusCode.COMMON_INVALID_SIGNATURE.httpCode
    ),

    //
    // USER
    // 2000 - 2999
    //

    USER_ALREADY_EXISTS(
        PrivatterStatusCode.USER_ALREADY_EXISTS.errorModel,
        PrivatterStatusCode.USER_ALREADY_EXISTS.httpCode
    ),
    USER_DOES_NOT_EXIST(
        PrivatterStatusCode.USER_DOES_NOT_EXIST.errorModel,
        PrivatterStatusCode.USER_DOES_NOT_EXIST.httpCode
    ),
    USER_VERIFICATION_REQUIRED(
        PrivatterStatusCode.USER_VERIFICATION_REQUIRED.errorModel,
        PrivatterStatusCode.USER_VERIFICATION_REQUIRED.httpCode
    ),
    USER_INVALID_VERIFICATION_TOKEN(
        PrivatterStatusCode.USER_INVALID_VERIFICATION_TOKEN.errorModel,
        PrivatterStatusCode.USER_INVALID_VERIFICATION_TOKEN.httpCode
    ),
    USER_ACTIVATE_ACCOUNT(
        PrivatterStatusCode.USER_ACTIVATE_ACCOUNT.errorModel,
        PrivatterStatusCode.USER_ACTIVATE_ACCOUNT.httpCode
    ),
    USER_INVALID_PASSWORD(
        PrivatterStatusCode.USER_INVALID_PASSWORD.errorModel,
        PrivatterStatusCode.USER_INVALID_PASSWORD.httpCode
    ),
    USER_DISABLED(
        PrivatterStatusCode.USER_DISABLED.errorModel,
        PrivatterStatusCode.USER_DISABLED.httpCode
    );
}
