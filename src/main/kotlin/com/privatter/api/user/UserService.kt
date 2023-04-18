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
    fun signUp(body: UserSignUpRequestModel, method: UserAuthMethod): UserSignUpResult = TODO()
}
