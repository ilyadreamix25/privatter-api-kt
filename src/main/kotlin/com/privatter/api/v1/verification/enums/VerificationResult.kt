package com.privatter.api.v1.verification.enums

enum class VerificationResult {
    INVALID_TOKEN_ID,
    INVALID_TOKEN_SECRET,
    EXPIRED,
    OK;
}
