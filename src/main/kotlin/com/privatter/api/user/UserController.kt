package com.privatter.api.user

import com.privatter.api.core.PrivatterResponseResource
import com.privatter.api.core.model.PrivatterEmptyResponseEntity
import com.privatter.api.core.model.PrivatterResponseEntity
import com.privatter.api.core.model.cast
import com.privatter.api.session.entity.SessionEntity
import com.privatter.api.user.enums.UserAccountVerificationResult
import com.privatter.api.user.enums.UserAuthMethod
import com.privatter.api.user.enums.UserSignUpResult
import com.privatter.api.user.model.*
import com.privatter.api.utility.parseEnumError
import jakarta.servlet.http.HttpServletRequest
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("user")
class UserController(private val service: UserService) {
    @PostMapping("sign-up")
    fun signUp(
        @RequestBody body: UserSignUpRequestModel,
        @RequestParam("method") method: String?,
        httpServletRequest: HttpServletRequest
    ): PrivatterEmptyResponseEntity {
        val invalidRequestEntity = PrivatterEmptyResponseEntity
            .badRequest()
            .body(PrivatterResponseResource.Model.INVALID_REQUEST)

        method ?: return invalidRequestEntity
        val authMethod = UserAuthMethod.of(method) ?: return invalidRequestEntity

        return when (service.signUp(body, authMethod, httpServletRequest.remoteAddr)) {
            UserSignUpResult.INVALID_METHOD -> PrivatterEmptyResponseEntity
                .badRequest()
                .body(PrivatterResponseResource.Model.INVALID_REQUEST)

            UserSignUpResult.INVALID_CAPTCHA -> PrivatterEmptyResponseEntity
                .badRequest()
                .body(PrivatterResponseResource.Model.INVALID_REQUEST)

            UserSignUpResult.USER_EXISTS -> PrivatterEmptyResponseEntity
                .badRequest()
                .body(PrivatterResponseResource.Model.USER_EXISTS)

            UserSignUpResult.VERIFICATION_REQUIRED -> PrivatterEmptyResponseEntity
                .ok()
                .body(PrivatterResponseResource.Model.USER_VERIFICATION_REQUIRED)
        }
    }

    @PostMapping("verify-account")
    fun verifyAccount(
        @RequestParam("user-id") userId: String?,
        @RequestParam("token-hash") tokenHash: String?,
        request: HttpServletRequest
    ): PrivatterResponseEntity<UserSignUpResponseModel> {
        val (verificationResult, user) = service.verifyAccount(userId, tokenHash)
        return when (verificationResult) {
            UserAccountVerificationResult.OK -> PrivatterResponseEntity.ok()
                .body(
                    PrivatterResponseResource.parseOk(
                        data = UserSignUpResponseModel(
                            user = UserModel(
                                id = user!!.id,
                                profile = UserModelProfile(
                                    nickname = user.profile.nickname,
                                    description = user.profile.description,
                                    iconUrl = user.profile.iconUrl
                                ),
                                metadata = UserModelMetadata(
                                    signedUpAt = user.metadata.signedUpAt,
                                    lastSignedInAt = user.metadata.lastSignedInAt
                                )
                            ),
                            sessionToken = service.createSessionToken(user.id, request.remoteAddr).second
                        )
                    )
                )

            else -> PrivatterResponseEntity.badRequest()
                .body(
                    PrivatterResponseResource
                        .parseError(
                            resource = PrivatterResponseResource.INVALID_REQUEST,
                            errorMessage = parseEnumError(UserAccountVerificationResult::class, verificationResult)
                        )
                        .cast()
                )
        }
    }

    @PostMapping("check-session")
    fun checkSession(session: SessionEntity) = session
}
