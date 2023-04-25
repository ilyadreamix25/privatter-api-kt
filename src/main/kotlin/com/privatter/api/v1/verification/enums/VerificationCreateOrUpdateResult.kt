package com.privatter.api.v1.verification.enums

enum class VerificationCreateOrUpdateResult(val resendMail: Boolean = false) {
    CREATED(true),
    UPDATED(true),
    SKIPPED
}
