package com.privatter.api.v1.verification

import com.privatter.api.v1.user.entity.UserEntity
import com.privatter.api.v1.verification.entity.VerificationEntity
import com.privatter.api.v1.verification.enums.VerificationAction
import com.privatter.api.v1.verification.enums.VerificationCreateOrUpdateResult
import com.privatter.api.v1.verification.enums.VerificationResult
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
class VerificationService(private val repository: VerificationRepository) {
    fun createOrUpdate(
        userId: String,
        action: VerificationAction,
        expirationTime: Long
    ): Pair<VerificationCreateOrUpdateResult, VerificationEntity> {
        val secret = generateTokenSecret()
        val verification = repository.findByUserId(userId, action.ordinal)

        if (verification == null) {
            val createdVerification = repository.save(
                VerificationEntity(
                    user = UserEntity(id = userId),
                    action = action,
                    expirationTime = expirationTime,
                    secret = secret
                )
            )
            return VerificationCreateOrUpdateResult.CREATED to createdVerification
        }

        if (System.currentTimeMillis() > verification.createdAt + verification.expirationTime) {
            verification.createdAt = System.currentTimeMillis()
            repository.save(verification)
            return VerificationCreateOrUpdateResult.UPDATED to verification
        }

        return VerificationCreateOrUpdateResult.SKIPPED to verification
    }

    fun verifyAndUpdate(tokenId: String, tokenSecret: String): Pair<VerificationResult, VerificationEntity?> {
        val verification = repository.findById(tokenId).getOrNull()
            ?: return VerificationResult.INVALID_TOKEN_ID to null

        if (System.currentTimeMillis() > verification.createdAt + verification.expirationTime)
            return VerificationResult.EXPIRED to null

        if (verification.secret != tokenSecret)
            return VerificationResult.INVALID_TOKEN_SECRET to null

        repository.delete(verification)

        return VerificationResult.OK to verification
    }

    fun find(userId: String, action: VerificationAction) = repository.findByUserId(userId, action.ordinal)

    private fun generateTokenSecret(): String {
        var secret = ""
        for (i in 1..6) {
            secret += (0..9).shuffled().first()
        }
        return secret
    }
}
