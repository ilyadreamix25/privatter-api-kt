package com.privatter.api.verification.models

import com.privatter.api.verification.entity.VerificationEntity

data class VerificationTokenModel(
    val info: VerificationEntity,
    val tokenHash: String
)
