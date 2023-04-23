package com.privatter.api.verification

import com.privatter.api.utility.toBase64
import com.privatter.api.utility.toSafeBase64
import com.privatter.api.verification.entity.VerificationEntity
import com.privatter.api.verification.enums.VerificationAction
import com.privatter.api.verification.enums.VerificationCreateOrUpdateResult
import com.privatter.api.verification.enums.VerificationResult
import com.privatter.api.verification.models.VerificationTokenModel
import org.springframework.stereotype.Service
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import kotlin.jvm.optionals.getOrNull

@Service
class VerificationService(
    private val properties: VerificationProperties,
    private val repository: VerificationRepository
) {
    fun createOrUpdate(
        userId: String,
        action: VerificationAction,
        expirationTime: Long
    ): Pair<VerificationCreateOrUpdateResult, VerificationTokenModel> {
        val verification = repository.findByUserId(userId, action.ordinal)
        if (verification == null) {
            val createdVerification = repository.save(
                VerificationEntity(
                    userId = userId,
                    action = action,
                    expirationTime = expirationTime
                )
            )
            return VerificationCreateOrUpdateResult.CREATED to VerificationTokenModel(
                createdVerification,
                generateTokenHash(createdVerification)
            )
        }

        if (verification.activated)
            return VerificationCreateOrUpdateResult.ALREADY_VERIFIED to VerificationTokenModel(
                verification,
                generateTokenHash(verification)
            )

        if (System.currentTimeMillis() > verification.createdAt + verification.expirationTime) {
            verification.createdAt = System.currentTimeMillis()
            repository.save(verification)
            return VerificationCreateOrUpdateResult.UPDATED to VerificationTokenModel(
                verification,
                generateTokenHash(verification)
            )
        }

        return VerificationCreateOrUpdateResult.SKIPPED to VerificationTokenModel(
            verification,
            generateTokenHash(verification)
        )
    }

    fun verifyAndUpdate(tokenId: String, tokenHash: String): VerificationResult {
        val verification = repository.findById(tokenId).getOrNull()
            ?: return VerificationResult.INVALID_USER_ID

        if (verification.activated)
            return VerificationResult.ALREADY_VERIFIED

        if (System.currentTimeMillis() > verification.createdAt + verification.expirationTime)
            return VerificationResult.EXPIRED

        if (generateTokenHash(verification) != tokenHash)
            return VerificationResult.INVALID_TOKEN_HASH

        verification.activated = true
        repository.save(verification)

        return VerificationResult.OK
    }

    private fun generateTokenHash(verification: VerificationEntity): String {
        val shortTokenInfo = verification.id + "|" +
            verification.action + "|" +
            verification.userId + "|" +
            verification.createdAt + "|" +
            verification.expirationTime

        val secretKeySpec = SecretKeySpec(properties.secret.toByteArray(), properties.algorithm)
        return Mac.getInstance(properties.algorithm)
            .apply {
                init(secretKeySpec)
            }
            .doFinal(shortTokenInfo.toByteArray())
            .toSafeBase64()
    }
}
