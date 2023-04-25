package com.privatter.api.v1.user.model

import com.privatter.api.v1.user.model.`object`.UserAuthModel
import com.privatter.api.v1.user.model.`object`.UserProfileModel
import com.privatter.api.v1.utility.validate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserSignUpRequestModel(
    @SerialName("auth")
    val auth: UserAuthModel,

    @SerialName("profile")
    val profile: UserProfileModel,

    @SerialName("captchaToken")
    val captchaToken: String? = null
) {
    init {
        validate(auth.key.isNotEmpty(), auth::key.name)
        validate(auth.value.isNotEmpty(), auth::value.name)

        validate(profile.nickname.length in 4..24, profile::nickname.name)
        validate(profile.nickname.matches("\\A[A-Za-z\\d_]+\\z".toRegex()), profile::nickname.name)
        validate(profile.nickname.toSet() != setOf("_"), profile::nickname.name)
        validate(profile.nickname.toSet().size >= 3, profile::nickname.name)

        captchaToken?.let {
            validate(it.isNotBlank(), ::captchaToken.name)
            validate(it.matches("[A-Za-z0-9_-]+".toRegex()), ::captchaToken.name)
        }
    }
}
