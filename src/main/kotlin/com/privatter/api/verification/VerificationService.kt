package com.privatter.api.verification

import com.privatter.api.verification.entity.VerificationEntity
import com.privatter.api.verification.enums.VerificationAction
import com.privatter.api.verification.enums.VerificationCreateOrUpdateResult
import org.springframework.stereotype.Service

@Service
class VerificationService(
    private val properties: VerificationProperties,
    private val repository: VerificationRepository
) {
    fun createOrUpdate(
        userId: String,
        action: VerificationAction,
        expirationTime: Long
    ): VerificationCreateOrUpdateResult {
        val verification = repository.findByUserId(userId, action.ordinal)
        if (verification == null) {
            repository.save(
                VerificationEntity(
                    userId = userId,
                    action = action,
                    expirationTime = expirationTime
                )
            )
            return VerificationCreateOrUpdateResult.CREATED
        }

        if (verification.activated)
            return VerificationCreateOrUpdateResult.ALREADY_VERIFIED

        if (System.currentTimeMillis() > verification.createdAt + verification.expirationTime) {
            verification.createdAt = System.currentTimeMillis()
            return VerificationCreateOrUpdateResult.UPDATED
        }

        return VerificationCreateOrUpdateResult.SKIPPED
    }
}
