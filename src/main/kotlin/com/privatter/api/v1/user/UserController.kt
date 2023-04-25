package com.privatter.api.v1.user

import com.privatter.api.v1.core.PrivatterResponseResource
import com.privatter.api.v1.core.model.PrivatterEmptyResponseEntity
import com.privatter.api.v1.core.model.PrivatterResponseEntity
import com.privatter.api.v1.core.model.PrivatterResponseModel
import com.privatter.api.v1.core.model.cast
import com.privatter.api.v1.user.enums.UserAuthMethod
import com.privatter.api.v1.user.enums.UserRequestActionVerificationResult
import com.privatter.api.v1.user.model.*
import com.privatter.api.v1.user.model.`object`.UserModel
import com.privatter.api.v1.user.model.`object`.UserMetadataModel
import com.privatter.api.v1.user.model.`object`.UserProfileModel
import com.privatter.api.v1.utility.isEmail
import com.privatter.api.v1.user.model.*
import com.privatter.api.v1.verification.enums.VerificationAction
import com.privatter.api.v1.verification.enums.VerificationResult
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
                        profile = UserProfileModel(
                            nickname = user.profile.nickname!!,
                            description = user.profile.description,
                            iconUrl = user.profile.iconUrl
                        ),
                        metadata = UserMetadataModel(
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
                        errorInformation = PrivatterResponseModel.ErrorInformation(
                            message = "Bad verification result",
                            stackTrace = listOf(result.toString())
                        )
                    )
                )

        if (info!!.action == VerificationAction.USER_SIGN_UP)
            service.verifyUser(info.user.id)

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
                            profile = UserProfileModel(
                                nickname = user.profile.nickname!!,
                                description = user.profile.description,
                                iconUrl = user.profile.iconUrl
                            ),
                            metadata = UserMetadataModel(
                                signedUpAt = user.metadata.signedUpAt,
                                lastSignedInAt = user.metadata.lastSignedInAt
                            )
                        ),
                        userSessionToken = service.createSessionToken(user.id, request.remoteAddr).second
                    )
                )
            )
    }

    // TODO: Delete account
    @PostMapping("delete-account")
    fun deleteAccount(
        @RequestBody body: UserVerificationTokenModel,
        @RequestParam("method") method: String?,
        request: HttpServletRequest
    ): Nothing = TODO()
}
