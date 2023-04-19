package com.privatter.api.user

import com.privatter.api.server.ServerProperties
import com.privatter.api.user.entity.UserEntity
import com.privatter.api.user.entity.UserEntityAuth
import com.privatter.api.user.entity.UserEntityProfile
import com.privatter.api.user.enums.UserAuthMethod
import com.privatter.api.user.enums.UserSignUpResult
import com.privatter.api.user.model.UserSignUpRequestModel
import com.privatter.api.verification.VerificationService
import com.privatter.api.verification.enums.VerificationAction
import org.springframework.stereotype.Service

/** 15 min */
private const val USER_SIGN_UP_VERIFICATION_TIME = 1000 * 60 * 15L

@Service
class UserService(
    private val repository: UserRepository,
    private val properties: UserProperties,
    private val verificationService: VerificationService
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
        if (body.captchaToken == null)
            return UserSignUpResult.INVALID_CAPTCHA

        val user = repository.find(
            authKey = body.authKey,
            profileNickname = body.profileNickname
        )

        if (user != null) {
            if (user.auth.verified)
                return UserSignUpResult.USER_EXISTS

            verificationService.createOrUpdate(
                user.id,
                VerificationAction.USER_SIGN_UP,
                USER_SIGN_UP_VERIFICATION_TIME
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

        verificationService.createOrUpdate(
            newUser.id,
            VerificationAction.USER_SIGN_UP,
            USER_SIGN_UP_VERIFICATION_TIME
        )

        return UserSignUpResult.VERIFICATION_REQUIRED
    }

    private fun continueSignUpForGoogleOauth(body: UserSignUpRequestModel) = UserSignUpResult.INVALID_METHOD
}
