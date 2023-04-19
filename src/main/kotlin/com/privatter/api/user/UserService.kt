package com.privatter.api.user

import com.privatter.api.mail.MailService
import com.privatter.api.recaptcha.ReCaptchaService
import com.privatter.api.user.entity.UserEntity
import com.privatter.api.user.entity.UserEntityAuth
import com.privatter.api.user.entity.UserEntityProfile
import com.privatter.api.user.enums.UserAuthMethod
import com.privatter.api.user.enums.UserSignUpResult
import com.privatter.api.user.model.UserSignUpRequestModel
import com.privatter.api.verification.VerificationService
import com.privatter.api.verification.enums.VerificationAction
import com.privatter.api.verification.enums.VerificationCreateOrUpdateResult
import org.springframework.stereotype.Service

/** 15 minutes */
private const val USER_SIGN_UP_VERIFICATION_TIME = 1000 * 60 * 15L

@Service
class UserService(
    private val repository: UserRepository,
    private val properties: UserProperties,
    private val verificationService: VerificationService,
    private val reCaptchaService: ReCaptchaService,
    private val mailService: MailService
) {
    fun signUp(body: UserSignUpRequestModel, method: UserAuthMethod): UserSignUpResult {
        if (method !in properties.authMethods)
            return UserSignUpResult.INVALID_METHOD

        return when (method) {
            UserAuthMethod.EMAIL -> continueSignUpForEmail(body)
            UserAuthMethod.GOOGLE_OAUTH -> continueSignUpForGoogleOauth(body)
        }
    }

    private fun continueSignUpForEmail(body: UserSignUpRequestModel): UserSignUpResult {
        if (body.captchaToken == null || !reCaptchaService.siteVerifyToken(body.captchaToken))
            return UserSignUpResult.INVALID_CAPTCHA

        val user = repository.find(
            authKey = body.authKey,
            profileNickname = body.profileNickname
        )

        if (user != null) {
            if (user.auth.verified)
                return UserSignUpResult.USER_EXISTS

            val resendMail = verificationService
                .createOrUpdate(
                    user.id,
                    VerificationAction.USER_SIGN_UP,
                    USER_SIGN_UP_VERIFICATION_TIME
                )
                .resendMail

            if (resendMail)
                mailService.sendMail(
                    to = body.authKey,
                    subject = "Privatter Sign Up",
                    body = "TEST"
                )

            return UserSignUpResult.VERIFICATION_REQUIRED
        }

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

        val resendMail = verificationService
            .createOrUpdate(
                newUser.id,
                VerificationAction.USER_SIGN_UP,
                USER_SIGN_UP_VERIFICATION_TIME
            )
            .resendMail

        if (resendMail)
            mailService.sendMail(
                to = body.authKey,
                subject = "Privatter Sign Up",
                body = "TEST"
            )

        return UserSignUpResult.VERIFICATION_REQUIRED
    }

    private fun continueSignUpForGoogleOauth(body: UserSignUpRequestModel) = UserSignUpResult.INVALID_METHOD
}
