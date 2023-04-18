package com.privatter.api.user.enums

enum class UserSignUpResult {
    OK,
    INVALID_REQUEST,
    INVALID_CAPTCHA,
    USER_EXISTS,
    VERIFICATION_REQUIRED
}
