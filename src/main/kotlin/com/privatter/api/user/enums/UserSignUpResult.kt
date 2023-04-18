package com.privatter.api.user.enums

enum class UserSignUpResult {
    OK,
    INVALID_METHOD,
    INVALID_CAPTCHA,
    USER_EXISTS,
    VERIFICATION_REQUIRED
}
