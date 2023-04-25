package com.privatter.api.v1.user.enums

import com.privatter.api.v1.core.PrivatterResponseResource
import com.privatter.api.v1.core.model.PrivatterEmptyResponseEntity
import com.privatter.api.v1.core.model.PrivatterResponseEntity
import com.privatter.api.v1.core.model.cast
import com.privatter.api.v1.user.model.UserSignInResponseModel

enum class UserSignInResult {
    INVALID_METHOD,
    INVALID_CAPTCHA,
    USER_DOES_NOT_EXIST,
    INVALID_AUTH_VALUE,
    ACCOUNT_IS_NOT_ACTIVATED,
    VERIFICATION_REQUIRED,
    INVALID_VERIFICATION,
    OK;

    fun parseEntity(): PrivatterResponseEntity<UserSignInResponseModel>? = when (this) {
        INVALID_METHOD -> PrivatterEmptyResponseEntity
            .badRequest()
            .body(PrivatterResponseResource.INVALID_REQUEST.asErrorModel.cast())

        INVALID_CAPTCHA -> PrivatterEmptyResponseEntity
            .badRequest()
            .body(PrivatterResponseResource.INVALID_REQUEST.asErrorModel.cast())

        USER_DOES_NOT_EXIST -> PrivatterEmptyResponseEntity
            .badRequest()
            .body(PrivatterResponseResource.USER_DOES_NOT_EXIST.asErrorModel.cast())

        INVALID_AUTH_VALUE -> PrivatterEmptyResponseEntity
            .badRequest()
            .body(PrivatterResponseResource.USER_INVALID_PASSWORD.asErrorModel.cast())

        ACCOUNT_IS_NOT_ACTIVATED -> PrivatterEmptyResponseEntity
            .badRequest()
            .body(PrivatterResponseResource.USER_ACCOUNT_NOT_ACTIVATED.asErrorModel.cast())

        VERIFICATION_REQUIRED -> PrivatterEmptyResponseEntity
            .badRequest()
            .body(PrivatterResponseResource.USER_VERIFICATION_REQUIRED.asErrorModel.cast())

        INVALID_VERIFICATION -> PrivatterEmptyResponseEntity
            .badRequest()
            .body(PrivatterResponseResource.USER_VERIFICATION_INVALID_DATA.asErrorModel.cast())

        else -> null
    }
}