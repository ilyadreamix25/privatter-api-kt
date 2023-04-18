package com.privatter.api.user.enums

enum class UserSignUpResult(val isSuccess: Boolean = false) {
    OK(true),
    INVALID_REQUEST,
    INVALID_CAPTCHA,
    USER_EXISTS,
    VERIFICATION_REQUIRED
}
