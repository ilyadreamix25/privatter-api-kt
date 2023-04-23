package com.privatter.api.user

import com.privatter.api.mail.MailService
import com.privatter.api.recaptcha.ReCaptchaService
import com.privatter.api.session.SessionService
import com.privatter.api.user.entity.UserEntity
import com.privatter.api.user.entity.UserEntityAuth
import com.privatter.api.user.entity.UserEntityProfile
import com.privatter.api.user.enums.UserAuthMethod
import com.privatter.api.user.enums.UserSignInResult
import com.privatter.api.user.enums.UserSignUpResult
import com.privatter.api.user.model.UserSignInRequestModel
import com.privatter.api.user.model.UserSignUpRequestModel
import com.privatter.api.utility.fromBase64ToByteArray
import com.privatter.api.utility.isEmail
import com.privatter.api.utility.toBase64
import com.privatter.api.utility.validate
import com.privatter.api.verification.VerificationService
import com.privatter.api.verification.enums.VerificationAction
import com.privatter.api.verification.enums.VerificationResult
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

/** 15 minutes */
private const val USER_SIGN_UP_VERIFICATION_TIME = 1000 * 60 * 15L

/** 15 minutes */
private const val USER_SIGN_IN_VERIFICATION_TIME = 1000 * 60 * 15L

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

    private fun continueSignUpForEmail(body: UserSignUpRequestModel, ip: String): Pair<UserSignUpResult, UserEntity?> {
        validate(body.captchaToken != null, body::captchaToken.name)
        validate(body.authKey.isEmail(), body::authKey.name)
        validate(body.authValue.length >= 6, body::authValue.name)

        if (!reCaptchaService.siteVerifyToken(body.captchaToken!!))
            return UserSignUpResult.INVALID_CAPTCHA to null

        val user = repository.find(
            authKey = body.authKey.encoded,
            profileNickname = body.profileNickname
        )

        if (user != null) {
            if (user.auth.verified)
                return UserSignUpResult.USER_EXISTS to null

            if (body.verificationTokenId != null) {
                return when (verificationService.verifyAndUpdate(body.verificationTokenId, body.verificationTokenHash!!)) {
                    VerificationResult.OK -> {
                        user.auth.verified = true
                        user.auth.verifiedAt = System.currentTimeMillis()
                        repository.save(user)

                        UserSignUpResult.OK to user
                    }
                    else -> UserSignUpResult.INVALID_VERIFICATION to null
                }
            }
        }

        try {
            val newUser = repository.save(
                UserEntity(
                    auth = UserEntityAuth(
                        key = body.authKey.encoded,
                        value = body.authValue.encoded,
                        ips = listOf(ip)
                    ),
                    profile = UserEntityProfile(
                        nickname = body.profileNickname,
                        description = body.profileDescription,
                        iconUrl = body.profileIconUrl
                    )
                )
            )

            createOrUpdateSignUpVerification(email = body.authKey, userId = newUser.id)

            return UserSignUpResult.VERIFICATION_REQUIRED to null
        } catch (_: DataIntegrityViolationException) {
            return UserSignUpResult.USER_EXISTS to null
        }
    }

    private fun continueSignUpForGoogleOauth(body: UserSignUpRequestModel, ip: String) = UserSignUpResult.INVALID_METHOD

    private fun continueSignInForEmail(body: UserSignInRequestModel, ip: String): Pair<UserSignInResult, UserEntity?> {
        validate(body.captchaToken != null, body::captchaToken.name)
        validate(body.authKey.isEmail(), body::authKey.name)
        validate(body.authValue.length >= 6, body::authValue.name)

        if (!reCaptchaService.siteVerifyToken(body.captchaToken!!))
            return UserSignInResult.INVALID_CAPTCHA to null

        val user = repository.findByAuthKey(body.authKey.encoded)
            ?: return UserSignInResult.USER_DOES_NOT_EXIST to null

        if (user.auth.value.decoded != body.authValue)
            return UserSignInResult.INVALID_AUTH_VALUE to null

        if (!user.auth.verified)
            return UserSignInResult.ACCOUNT_IS_NOT_ACTIVATED to null

        var verificationRequired = false

        if (ip !in user.auth.ips)
            verificationRequired = true

        if (System.currentTimeMillis() > user.metadata.lastSignedInAt + USER_LOG_OUT_TIME && user.metadata.lastSignedInAt != 0L)
            verificationRequired = true

        if (verificationRequired) {
            if (body.verificationTokenId != null)
                return when (verificationService.verifyAndUpdate(body.verificationTokenId, body.verificationTokenHash!!)) {
                    VerificationResult.OK -> {
                        if (ip !in user.auth.ips) {
                            val newIps = user.auth.ips.toMutableList()
                            newIps.add(ip)
                            user.auth.ips = newIps
                        }
                        user.metadata.lastSignedInAt = System.currentTimeMillis()
                        repository.save(user)

                        UserSignInResult.OK to user
                    }
                    else -> UserSignInResult.INVALID_VERIFICATION to null
                }

            createOrUpdateSignInVerification(email = body.authKey, userId = user.id)

            return UserSignInResult.VERIFICATION_REQUIRED to null
        }

        user.metadata.lastSignedInAt = System.currentTimeMillis()
        repository.save(user)

        return UserSignInResult.OK to user
    }

    private fun continueSignInForGoogleOauth(body: UserSignInRequestModel, ip: String) = UserSignInResult.INVALID_METHOD

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
                    "privatter://user/finish-sign-up" +
                    "?token-id=${verification.second.info.id}&token-hash=${verification.second.tokenHash}"
            )
    }

    private fun createOrUpdateSignInVerification(email: String, userId: String) {
        val verification = verificationService.createOrUpdate(
            userId = userId,
            action = VerificationAction.USER_SIGN_IN,
            expirationTime = USER_SIGN_IN_VERIFICATION_TIME
        )

        if (verification.first.resendMail)
            mailService.sendMail(
                to = email,
                subject = "Privatter Sign In",
                body = "Finish your Privatter account sign in: " +
                    "privatter://user/finish-sign-in" +
                    "?token-id=${verification.second.info.id}&token-hash=${verification.second.tokenHash}"
            )
    }

    private fun encodeString(value: String): String {
        val secretKeySpec = SecretKeySpec(properties.secret.toByteArray(), properties.algorithm)
        val cipher = Cipher.getInstance(properties.algorithm)
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec)
        return cipher.doFinal(value.toByteArray()).toBase64()
    }

    private val String.encoded get() = encodeString(this)

    private fun decodeString(value: String): String {
        val secretKeySpec = SecretKeySpec(properties.secret.toByteArray(), properties.algorithm)
        val cipher = Cipher.getInstance(properties.algorithm)
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec)
        return String(cipher.doFinal(value.fromBase64ToByteArray()))
    }

    private val String.decoded get() = decodeString(this)
}
