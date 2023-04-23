package com.privatter.api.verification.enums

enum class VerificationResult {
    INVALID_TOKEN_ID,
    INVALID_TOKEN_SECRET,
    EXPIRED,
    ALREADY_VERIFIED,
    OK
}
