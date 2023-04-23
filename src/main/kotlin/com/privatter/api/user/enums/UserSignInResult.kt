package com.privatter.api.user.enums

import com.privatter.api.core.PrivatterResponseResource
import com.privatter.api.core.model.PrivatterEmptyResponseEntity
import com.privatter.api.core.model.PrivatterResponseEntity
import com.privatter.api.core.model.cast
import com.privatter.api.user.model.UserSignInResponseModel

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
            .body(PrivatterResponseResource.INVALID_REQUEST.asModel.cast())

        INVALID_CAPTCHA -> PrivatterEmptyResponseEntity
            .badRequest()
            .body(PrivatterResponseResource.INVALID_REQUEST.asModel.cast())

        USER_DOES_NOT_EXIST -> PrivatterEmptyResponseEntity
            .badRequest()
            .body(PrivatterResponseResource.USER_DOES_NOT_EXIST.asModel.cast())

        INVALID_AUTH_VALUE -> PrivatterEmptyResponseEntity
            .badRequest()
            .body(PrivatterResponseResource.USER_INVALID_PASSWORD.asModel.cast())

        ACCOUNT_IS_NOT_ACTIVATED -> PrivatterEmptyResponseEntity
            .badRequest()
            .body(PrivatterResponseResource.USER_ACCOUNT_NOT_ACTIVATED.asModel.cast())

        VERIFICATION_REQUIRED -> PrivatterEmptyResponseEntity
            .badRequest()
            .body(PrivatterResponseResource.USER_VERIFICATION_REQUIRED.asModel.cast())

        INVALID_VERIFICATION -> PrivatterEmptyResponseEntity
            .badRequest()
            .body(PrivatterResponseResource.USER_VERIFICATION_INVALID_DATA.asModel.cast())

        else -> null
    }
}