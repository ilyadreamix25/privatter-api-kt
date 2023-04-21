package com.privatter.api.verification.enums

enum class VerificationResult {
    INVALID_USER_ID,
    INVALID_TOKEN_HASH,
    EXPIRED,
    ALREADY_VERIFIED,
    OK
}
