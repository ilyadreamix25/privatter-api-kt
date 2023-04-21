package com.privatter.api.user.model

import com.privatter.api.utility.validate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserSignUpRequestModel(
    @SerialName("authKey")
    val authKey: String,

    @SerialName("authValue")
    val authValue: String,

    @SerialName("profileNickname")
    val profileNickname: String,

    @SerialName("profileDescription")
    val profileDescription: String? = null,

    @SerialName("profileIconUrl")
    val profileIconUrl: String? = null,

    @SerialName("captchaToken")
    val captchaToken: String? = null
) {
    init {
        validate(authKey.isNotEmpty(), UserSignUpRequestModel::authKey.name)
        validate(authValue.isNotEmpty(), UserSignUpRequestModel::authValue.name)

        validate(profileNickname.length in 4..24, UserSignUpRequestModel::profileNickname.name)
        validate(profileNickname.matches("\\A[A-Za-z\\d_]+\\z".toRegex()), UserSignUpRequestModel::profileNickname.name)
        validate(profileNickname.toSet() != setOf("_"), UserSignUpRequestModel::profileNickname.name)
        validate(profileNickname.toSet().size >= 3, UserSignUpRequestModel::profileNickname.name)

        captchaToken?.let {
            validate(it.isNotBlank(), UserSignUpRequestModel::captchaToken.name)
            validate(it.matches("[A-Za-z0-9_-]+".toRegex()), UserSignUpRequestModel::captchaToken.name)
        }
    }
}
