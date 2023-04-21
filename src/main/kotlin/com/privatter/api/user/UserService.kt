package com.privatter.api.user

import com.privatter.api.mail.MailService
import com.privatter.api.recaptcha.ReCaptchaService
import com.privatter.api.server.ServerProperties
import com.privatter.api.user.entity.UserEntity
import com.privatter.api.user.entity.UserEntityAuth
import com.privatter.api.user.entity.UserEntityProfile
import com.privatter.api.user.enums.UserAccountVerificationResult
import com.privatter.api.user.enums.UserAuthMethod
import com.privatter.api.user.enums.UserSignUpResult
import com.privatter.api.user.model.UserSignUpRequestModel
import com.privatter.api.utility.isEmail
import com.privatter.api.utility.validate
import com.privatter.api.verification.VerificationService
import com.privatter.api.verification.enums.VerificationAction
import com.privatter.api.verification.enums.VerificationResult
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service

/** 15 minutes */
private const val USER_SIGN_UP_VERIFICATION_TIME = 1000 * 60 * 15L

@Service
class UserService(
    private val repository: UserRepository,
    private val properties: UserProperties,
    private val verificationService: VerificationService,
    private val reCaptchaService: ReCaptchaService,
    private val mailService: MailService,
    private val serverProperties: ServerProperties
) {
    fun signUp(body: UserSignUpRequestModel, method: UserAuthMethod): UserSignUpResult {
        if (method !in properties.authMethods)
            return UserSignUpResult.INVALID_METHOD

        return when (method) {
            UserAuthMethod.EMAIL -> continueSignUpForEmail(body)
            UserAuthMethod.GOOGLE_OAUTH -> continueSignUpForGoogleOauth(body)
        }
    }

    fun verifyAccount(userId: String?, tokenHash: String?): Pair<UserAccountVerificationResult, UserEntity?> {
        if (userId == null || tokenHash == null)
            return UserAccountVerificationResult.INVALID_REQUEST to null

        when (verificationService.verifyAndUpdate(userId, tokenHash, VerificationAction.USER_SIGN_UP)) {
            VerificationResult.INVALID_USER_ID -> return UserAccountVerificationResult.INVALID_USER_ID to null
            VerificationResult.INVALID_TOKEN_HASH -> return UserAccountVerificationResult.INVALID_TOKEN_HASH to null
            VerificationResult.EXPIRED -> return UserAccountVerificationResult.EXPIRED to null
            VerificationResult.ALREADY_VERIFIED -> return UserAccountVerificationResult.ALREADY_VERIFIED to null
            VerificationResult.OK -> {}
        }

        val user = repository.find(userId) ?: return UserAccountVerificationResult.INVALID_USER_ID to null
        user.auth.verified = true
        user.auth.verifiedAt = System.currentTimeMillis()
        repository.save(user)

        return UserAccountVerificationResult.OK to user
    }

    private fun continueSignUpForEmail(body: UserSignUpRequestModel): UserSignUpResult {
        validate(body.captchaToken != null, body::captchaToken.name)
        validate(body.authKey.isEmail(), body::authKey.name)
        validate(body.authValue.length < 6, body::authValue.name)

        if (!reCaptchaService.siteVerifyToken(body.captchaToken!!))
            return UserSignUpResult.INVALID_CAPTCHA

        val user = repository.find(
            authKey = body.authKey,
            profileNickname = body.profileNickname
        )

        if (user != null) {
            if (user.auth.verified)
                return UserSignUpResult.USER_EXISTS

            createOrUpdateSignUpVerification(email = body.authKey, userId = user.id)

            return UserSignUpResult.VERIFICATION_REQUIRED
        }

        try {
            val newUser = repository.save(
                UserEntity(
                    auth = UserEntityAuth(
                        key = body.authKey,
                        value = body.authValue
                    ),
                    profile = UserEntityProfile(
                        nickname = body.profileNickname,
                        description = body.profileDescription,
                        iconUrl = body.profileIconUrl
                    )
                )
            )

            createOrUpdateSignUpVerification(email = body.authKey, userId = newUser.id)

            return UserSignUpResult.VERIFICATION_REQUIRED
        } catch (_: DataIntegrityViolationException) {
            return UserSignUpResult.USER_EXISTS
        }
    }

    private fun continueSignUpForGoogleOauth(body: UserSignUpRequestModel) = UserSignUpResult.INVALID_METHOD

    private fun createOrUpdateSignUpVerification(email: String, userId: String) {
        val verification = verificationService.createOrUpdate(
            userId = userId,
            action = VerificationAction.USER_SIGN_UP,
            expirationTime = USER_SIGN_UP_VERIFICATION_TIME
        )

        if (verification.first.resendMail)
            mailService.sendMail(
                to = email,
                subject = "Privatter Sign Up",
                body = "Finish your Privatter account sign up: " +
                    "${serverProperties.url}/user/verify-account" +
                    "?user-id=$userId&token-hash=${verification.second.tokenHash}"
            )
    }
}
