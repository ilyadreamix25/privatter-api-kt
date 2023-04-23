package com.privatter.api.verification.enums

enum class VerificationCreateOrUpdateResult(val resendMail: Boolean = false) {
    CREATED(true),
    UPDATED(true),
    SKIPPED
}
