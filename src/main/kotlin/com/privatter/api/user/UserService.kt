package com.privatter.api.user

import com.privatter.api.mail.MailService
import com.privatter.api.recaptcha.ReCaptchaService
import com.privatter.api.session.SessionService
import com.privatter.api.user.entity.UserEntity
import com.privatter.api.user.entity.UserEntityAuth
import com.privatter.api.user.entity.UserEntityProfile
import com.privatter.api.user.enums.UserAuthMethod
import com.privatter.api.user.enums.UserRequestActionVerificationResult
import com.privatter.api.user.enums.UserSignInResult
import com.privatter.api.user.enums.UserSignUpResult
import com.privatter.api.user.model.UserSignInRequestModel
import com.privatter.api.user.model.UserSignUpRequestModel
import com.privatter.api.utility.isEmail
import com.privatter.api.utility.toBase64
import com.privatter.api.utility.validate
import com.privatter.api.verification.VerificationService
import com.privatter.api.verification.enums.VerificationAction
import org.springframework.stereotype.Service
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

/** 3 minutes */
private const val USER_VERIFICATION_TIME = 1000 * 60 * 3L

/** One day */
private const val USER_SESSION_EXPIRATION_TIME = 1000 * 60 * 60 * 24L

/** One month */
private const val USER_LOG_OUT_TIME = 1000 * 60 * 60 * 24 * 30L

@Service
class UserService(
    private val repository: UserRepository,
    private val properties: UserProperties,
    private val verificationService: VerificationService,
    private val reCaptchaService: ReCaptchaService,
    private val sessionService: SessionService,
    private val mailService: MailService
) {
    fun signUp(body: UserSignUpRequestModel, method: UserAuthMethod, ip: String): Pair<UserSignUpResult, UserEntity?> {
        if (method !in properties.authMethods)
            return UserSignUpResult.INVALID_METHOD to null

        return when (method) {
            UserAuthMethod.EMAIL -> continueSignUpForEmail(body, ip)
            UserAuthMethod.GOOGLE_OAUTH -> continueSignUpForGoogleOauth(body, ip) to null
        }
    }

    fun signIn(body: UserSignInRequestModel, method: UserAuthMethod, ip: String): Pair<UserSignInResult, UserEntity?> {
        if (method !in properties.authMethods)
            return UserSignInResult.INVALID_METHOD to null

        return when (method) {
            UserAuthMethod.EMAIL -> continueSignInForEmail(body, ip)
            UserAuthMethod.GOOGLE_OAUTH -> continueSignInForGoogleOauth(body, ip) to null
        }
    }

    fun createSessionToken(userId: String, ip: String) =
        sessionService.create(userId, ip, USER_SESSION_EXPIRATION_TIME)

    fun requestActionVerification(
        email: String,
        action: VerificationAction
    ): Pair<UserRequestActionVerificationResult, String?> {
        val user = repository.findByAuthKey(email)
            ?: return UserRequestActionVerificationResult.USER_DOES_NOT_EXIST to null

        val (result, token) = verificationService.createOrUpdate(
            userId = user.id,
            action = action,
            expirationTime = USER_VERIFICATION_TIME
        )

        if (result.resendMail)
            mailService.sendMail(
                to = email,
                subject = "Privatter Verification",
                body = "Here's your Privatter code: ${token.secret}"
            )

        return UserRequestActionVerificationResult.OK to token.id
    }

    fun verifyToken(
        tokenId: String,
        tokenSecret: String
    ) = verificationService.verifyAndUpdate(tokenId, tokenSecret)

    fun verifyUser(userId: String) {
        val user = repository.findById(userId).get()
        user.auth.verified = true
        user.auth.verifiedAt = System.currentTimeMillis()
        repository.save(user)
    }

    private fun continueSignUpForEmail(body: UserSignUpRequestModel, ip: String): Pair<UserSignUpResult, UserEntity?> {
        validate(body.captchaToken != null, body::captchaToken.name)
        validate(body.authKey.isEmail(), body::authKey.name)
        validate(body.authValue.length >= 6, body::authValue.name)

        if (!reCaptchaService.siteVerifyToken(body.captchaToken!!))
            return UserSignUpResult.INVALID_CAPTCHA to null

        val user = repository.find(
            authKey = body.authKey,
            profileNickname = body.profileNickname
        )

        if (user != null)
            return UserSignUpResult.USER_EXISTS to null

        val newUser = repository.save(
            UserEntity(
                auth = UserEntityAuth(
                    key = body.authKey,
                    value = generatePasswordHash(body.authValue),
                    ips = listOf(ip)
                ),
                profile = UserEntityProfile(
                    nickname = body.profileNickname,
                    description = body.profileDescription,
                    iconUrl = body.profileIconUrl
                )
            )
        )

        return UserSignUpResult.OK to newUser
    }

    private fun continueSignUpForGoogleOauth(body: UserSignUpRequestModel, ip: String) = UserSignUpResult.INVALID_METHOD

    private fun continueSignInForEmail(body: UserSignInRequestModel, ip: String): Pair<UserSignInResult, UserEntity?> {
        validate(body.captchaToken != null, body::captchaToken.name)
        validate(body.authKey.isEmail(), body::authKey.name)
        validate(body.authValue.length >= 6, body::authValue.name)

        if (!reCaptchaService.siteVerifyToken(body.captchaToken!!))
            return UserSignInResult.INVALID_CAPTCHA to null

        val user = repository.findByAuthKey(body.authKey)
            ?: return UserSignInResult.USER_DOES_NOT_EXIST to null

        if (user.auth.value != generatePasswordHash(body.authValue))
            return UserSignInResult.INVALID_AUTH_VALUE to null

        if (!user.auth.verified)
            return UserSignInResult.ACCOUNT_IS_NOT_ACTIVATED to null

        var verificationRequired = false

        if (ip !in user.auth.ips)
            verificationRequired = true

        if (System.currentTimeMillis() > user.metadata.lastSignedInAt + USER_LOG_OUT_TIME && user.metadata.lastSignedInAt != 0L)
            verificationRequired = true

        if (verificationRequired) {
            val verification = verificationService.find(user.id, VerificationAction.USER_SIGN_IN)
                ?: return UserSignInResult.VERIFICATION_REQUIRED to null

            if (System.currentTimeMillis() > verification.createdAt + verification.expirationTime)
                return UserSignInResult.VERIFICATION_REQUIRED to null
        }

        user.metadata.lastSignedInAt = System.currentTimeMillis()
        repository.save(user)

        return UserSignInResult.OK to user
    }

    private fun continueSignInForGoogleOauth(body: UserSignInRequestModel, ip: String) = UserSignInResult.INVALID_METHOD

    private fun generatePasswordHash(value: String): String {
        val secretKeySpec = SecretKeySpec(properties.secret.toByteArray(), properties.algorithm)
        return Mac.getInstance(properties.algorithm)
            .apply {
                init(secretKeySpec)
            }
            .doFinal(value.toByteArray())
            .toBase64()
    }
}
