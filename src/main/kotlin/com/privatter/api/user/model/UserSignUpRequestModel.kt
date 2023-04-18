package com.privatter.api.user.model

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
        require(authKey.isNotEmpty())
        require(authValue.isNotEmpty())
        require(profileNickname.isNotEmpty())
        require(profileNickname.length in 4..24)
        require(profileNickname.matches("\\\\A[A-Za-z\\\\d_]+\\\\z".toRegex()))
    }
}
