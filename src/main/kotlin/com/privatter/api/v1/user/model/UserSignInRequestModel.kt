package com.privatter.api.v1.user.model

import com.privatter.api.v1.user.model.`object`.UserAuthModel
import com.privatter.api.v1.utility.validate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserSignInRequestModel(
    @SerialName("auth")
    val auth: UserAuthModel,

    @SerialName("captchaToken")
    val captchaToken: String? = null,

    @SerialName("verificationToken")
    val verificationTokenId: UserVerificationTokenModel? = null
) {
    init {
        validate(auth.key.isNotEmpty(), auth::key.name)
        validate(auth.value.isNotEmpty(), auth::value.name)

        captchaToken?.let {
            validate(it.isNotBlank(), ::captchaToken.name)
            validate(it.matches("[A-Za-z0-9_-]+".toRegex()), ::captchaToken.name)
        }
    }
}
