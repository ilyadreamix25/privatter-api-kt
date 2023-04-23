package com.privatter.api.user

import com.privatter.api.core.PrivatterResponseResource
import com.privatter.api.core.model.PrivatterEmptyResponseEntity
import com.privatter.api.core.model.PrivatterResponseEntity
import com.privatter.api.core.model.PrivatterResponseModel
import com.privatter.api.core.model.cast
import com.privatter.api.user.enums.UserAuthMethod
import com.privatter.api.user.model.*
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
    ): PrivatterResponseEntity<UserSignUpResponseModel> {
        val invalidRequestEntity = PrivatterEmptyResponseEntity
            .badRequest()
            .body(PrivatterResponseResource.INVALID_REQUEST.asModel.cast<UserSignUpResponseModel>())

        method ?: return invalidRequestEntity
        val authMethod = UserAuthMethod.of(method) ?: return invalidRequestEntity

        val (result, user) = service.signUp(body, authMethod, request.remoteAddr)
        return result.parseEntity() ?: PrivatterResponseEntity.ok()
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
                        userSessionToken = service.createSessionToken(user.id, request.remoteAddr).second
                    )
                )
            )
    }

    @PostMapping("sign-in")
    fun signIn(
        @RequestBody body: UserSignInRequestModel,
        @RequestParam("method") method: String?,
        request: HttpServletRequest
    ): ResponseEntity<PrivatterResponseModel<UserSignInResponseModel>> {
        val invalidRequestEntity = PrivatterEmptyResponseEntity
            .badRequest()
            .body(PrivatterResponseResource.INVALID_REQUEST.asModel.cast<UserSignInResponseModel>())

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
