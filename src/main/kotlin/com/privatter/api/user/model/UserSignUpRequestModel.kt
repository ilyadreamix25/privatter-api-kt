package com.privatter.api.user.model

import com.privatter.api.utility.validateFromContext
import com.privatter.api.utility.validateFromRequirement
import com.privatter.api.validation.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserSignUpRequestModel(
    @IsNotEmpty
    @SerialName("authKey")
    val authKey: String,

    @IsNotEmpty
    @SerialName("authValue")
    val authValue: String,

    @IsNotEmpty
    @LengthBetween(4, 24)
    @Matches("\\A[A-Za-z\\d_]+\\z")
    @SerialName("profileNickname")
    val profileNickname: String,

    @SerialName("profileDescription")
    val profileDescription: String? = null,

    @SerialName("profileIconUrl")
    val profileIconUrl: String? = null,

    @IsNotBlank
    @Matches("[A-Za-z0-9_-]+")
    @SerialName("captchaToken")
    val captchaToken: String? = null
) {
    init {
        validateFromRequirement(profileNickname.toSet() != setOf("_"), "Invalid profileNickname")
        validateFromRequirement(profileNickname.toSet().size >= 3, "Invalid profileNickname")

        captchaToken?.let {
            validateFromRequirement(it.isNotBlank(), "captchaToken must not be blank")
        }

        validateFromContext(this)
    }
}
