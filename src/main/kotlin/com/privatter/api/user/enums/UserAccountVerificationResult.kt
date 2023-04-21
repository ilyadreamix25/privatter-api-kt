package com.privatter.api.user.enums

enum class UserAccountVerificationResult {
    INVALID_REQUEST,
    INVALID_USER_ID,
    INVALID_TOKEN_HASH,
    EXPIRED,
    ALREADY_VERIFIED,
    OK
}
