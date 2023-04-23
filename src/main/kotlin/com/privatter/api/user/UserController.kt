package com.privatter.api.user

import com.privatter.api.core.PrivatterResponseResource
import com.privatter.api.core.model.PrivatterEmptyResponseEntity
import com.privatter.api.core.model.PrivatterResponseEntity
import com.privatter.api.core.model.PrivatterResponseModel
import com.privatter.api.core.model.cast
import com.privatter.api.user.enums.UserAuthMethod
import com.privatter.api.user.enums.UserRequestActionVerificationResult
import com.privatter.api.user.model.*
import com.privatter.api.utility.isEmail
import com.privatter.api.verification.enums.VerificationAction
import com.privatter.api.verification.enums.VerificationResult
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity
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
        request: HttpServletRequest
    ): PrivatterResponseEntity<UserModel> {
        val invalidRequestEntity = PrivatterEmptyResponseEntity
            .badRequest()
            .body(PrivatterResponseResource.INVALID_REQUEST.asErrorModel.cast<UserModel>())

        method ?: return invalidRequestEntity
        val authMethod = UserAuthMethod.of(method) ?: return invalidRequestEntity

        val (result, user) = service.signUp(body, authMethod, request.remoteAddr)
        return result.parseEntity() ?: PrivatterResponseEntity.ok()
            .body(
                PrivatterResponseResource.parseOk(
                    data = UserModel(
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
                    )
                )
            )
    }

    @PostMapping("request-action-verification")
    fun requestActionVerification(
        @RequestParam("email") email: String?,
        @RequestParam("action") action: String?
    ): PrivatterResponseEntity<UserRequestActionVerificationResponse> {
        val invalidRequestEntity = PrivatterEmptyResponseEntity
            .badRequest()
            .body(PrivatterResponseResource.INVALID_REQUEST.asErrorModel.cast<UserRequestActionVerificationResponse>())

        action ?: return invalidRequestEntity
        val verificationAction = VerificationAction.of(action) ?: return invalidRequestEntity

        if (email == null || !email.isEmail())
            return invalidRequestEntity

        val (result, tokenId) = service.requestActionVerification(email, verificationAction)
        if (result == UserRequestActionVerificationResult.USER_DOES_NOT_EXIST)
            return ResponseEntity
                .badRequest()
                .body(PrivatterResponseResource.USER_DOES_NOT_EXIST.asErrorModel.cast())

        return ResponseEntity
            .ok()
            .body(
                PrivatterResponseResource.parseOk(
                    data = UserRequestActionVerificationResponse(tokenId!!)
                )
            )
    }

    @PostMapping("verify-token")
    fun verifyToken(
        @RequestParam("token-id") tokenId: String?,
        @RequestParam("token-secret") tokenSecret: String?
    ): PrivatterEmptyResponseEntity {
        if (tokenId == null || tokenSecret == null)
            return PrivatterEmptyResponseEntity
                .badRequest()
                .body(PrivatterResponseResource.INVALID_REQUEST.asErrorModel)

        val (result, info) = service.verifyToken(tokenId, tokenSecret)
        if (result != VerificationResult.OK)
            return PrivatterEmptyResponseEntity
                .badRequest()
                .body(
                    PrivatterResponseResource.parseError(
                        resource = PrivatterResponseResource.INVALID_REQUEST,
                        errorMessage = "Bad verification result",
                        errorInformation = listOf(result.name)
                    )
                )

        if (info!!.action == VerificationAction.USER_SIGN_UP)
            service.verifyUser(info.userId)

        return PrivatterEmptyResponseEntity
            .ok()
            .body(PrivatterResponseResource.OK_MODEL)
    }

    @PostMapping("sign-in")
    fun signIn(
        @RequestBody body: UserSignInRequestModel,
        @RequestParam("method") method: String?,
        request: HttpServletRequest
    ): ResponseEntity<PrivatterResponseModel<UserSignInResponseModel>> {
        val invalidRequestEntity = PrivatterEmptyResponseEntity
            .badRequest()
            .body(PrivatterResponseResource.INVALID_REQUEST.asErrorModel.cast<UserSignInResponseModel>())

        method ?: return invalidRequestEntity
        val authMethod = UserAuthMethod.of(method) ?: return invalidRequestEntity

        val (result, user) = service.signIn(body, authMethod, request.remoteAddr)
        return result.parseEntity() ?: PrivatterResponseEntity.ok()
            .body(
                PrivatterResponseResource.parseOk(
                    data = UserSignInResponseModel(
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
                        userSessionToken = service.createSessionToken(user.id, request.remoteAddr).second
                    )
                )
            )
    }
}
