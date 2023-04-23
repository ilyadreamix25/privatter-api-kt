package com.privatter.api.user.enums

import com.privatter.api.core.PrivatterResponseResource
import com.privatter.api.core.model.PrivatterEmptyResponseEntity
import com.privatter.api.core.model.PrivatterResponseEntity
import com.privatter.api.core.model.cast
import com.privatter.api.user.model.UserSignUpResponseModel

enum class UserSignUpResult {
    INVALID_METHOD,
    INVALID_CAPTCHA,
    USER_EXISTS,
    VERIFICATION_REQUIRED,
    INVALID_VERIFICATION,
    OK;

    fun parseEntity(): PrivatterResponseEntity<UserSignUpResponseModel>? = when (this) {
        INVALID_METHOD -> PrivatterEmptyResponseEntity
            .badRequest()
            .body(PrivatterResponseResource.Model.INVALID_REQUEST.cast())

        INVALID_CAPTCHA -> PrivatterEmptyResponseEntity
            .badRequest()
            .body(PrivatterResponseResource.Model.INVALID_REQUEST.cast())

        USER_EXISTS -> PrivatterEmptyResponseEntity
            .badRequest()
            .body(PrivatterResponseResource.Model.USER_EXISTS.cast())

        VERIFICATION_REQUIRED -> PrivatterEmptyResponseEntity
            .badRequest()
            .body(PrivatterResponseResource.Model.USER_VERIFICATION_REQUIRED.cast())

        INVALID_VERIFICATION -> PrivatterEmptyResponseEntity
            .badRequest()
            .body(PrivatterResponseResource.Model.USER_VERIFICATION_INVALID_DATA.cast())

        else -> null
    }
}
