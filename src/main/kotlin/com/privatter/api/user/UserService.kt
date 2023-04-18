package com.privatter.api.user

import com.privatter.api.user.enums.UserAuthMethod
import com.privatter.api.user.enums.UserSignUpResult
import com.privatter.api.user.model.UserSignUpRequestModel
import org.springframework.stereotype.Service

@Service
class UserService(
    private val repository: UserRepository,
    private val properties: UserProperties
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
        ) ?: return UserSignUpResult.OK

        if (user.auth.verified)
            return UserSignUpResult.USER_EXISTS

        return UserSignUpResult.VERIFICATION_REQUIRED
    }

    private fun continueSignUpForGoogleOauth(body: UserSignUpRequestModel) = UserSignUpResult.INVALID_METHOD
}
