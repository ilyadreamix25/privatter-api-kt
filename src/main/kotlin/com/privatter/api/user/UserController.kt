package com.privatter.api.user

import com.privatter.api.core.PrivatterResponseResource
import com.privatter.api.core.model.PrivatterEmptyResponseEntity
import com.privatter.api.user.enums.UserAuthMethod
import com.privatter.api.user.enums.UserSignUpResult
import com.privatter.api.user.model.UserSignUpRequestModel
import org.springframework.data.repository.query.Param
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("user")
class UserController(private val service: UserService) {
    @PostMapping("sign-up")
    fun signUp(
        @RequestBody body: UserSignUpRequestModel,
        @Param("method") method: String? = null
    ): PrivatterEmptyResponseEntity {
        val invalidRequestEntity = PrivatterEmptyResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(PrivatterResponseResource.Model.INVALID_REQUEST)

        method ?: return invalidRequestEntity
        val authMethod = UserAuthMethod.of(method) ?: return invalidRequestEntity

        return when (service.signUp(body, authMethod)) {
            UserSignUpResult.INVALID_METHOD -> PrivatterEmptyResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(PrivatterResponseResource.Model.INVALID_REQUEST)

            UserSignUpResult.INVALID_CAPTCHA -> PrivatterEmptyResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(PrivatterResponseResource.Model.INVALID_REQUEST)

            UserSignUpResult.USER_EXISTS -> PrivatterEmptyResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(PrivatterResponseResource.Model.USER_EXISTS)

            UserSignUpResult.VERIFICATION_REQUIRED -> PrivatterEmptyResponseEntity
                .ok()
                .body(PrivatterResponseResource.Model.USER_VERIFICATION_REQUIRED)
        }
    }
}
