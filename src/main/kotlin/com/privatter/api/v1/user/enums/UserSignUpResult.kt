package com.privatter.api.v1.user.enums

import com.privatter.api.v1.core.PrivatterResponseResource
import com.privatter.api.v1.core.model.PrivatterEmptyResponseEntity
import com.privatter.api.v1.core.model.PrivatterResponseEntity
import com.privatter.api.v1.core.model.cast
import com.privatter.api.v1.user.model.`object`.UserModel

enum class UserSignUpResult {
    INVALID_METHOD,
    INVALID_CAPTCHA,
    USER_EXISTS,
    OK;

    fun parseEntity(): PrivatterResponseEntity<UserModel>? = when (this) {
        INVALID_METHOD -> PrivatterEmptyResponseEntity
            .badRequest()
            .body(PrivatterResponseResource.INVALID_REQUEST.asErrorModel.cast())

        INVALID_CAPTCHA -> PrivatterEmptyResponseEntity
            .badRequest()
            .body(PrivatterResponseResource.INVALID_REQUEST.asErrorModel.cast())

        USER_EXISTS -> PrivatterEmptyResponseEntity
            .badRequest()
            .body(PrivatterResponseResource.USER_EXISTS.asErrorModel.cast())

        else -> null
    }
}
