package com.privatter.api.recaptcha.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReCaptchaTokenVerificationResultModel(
    @SerialName("success")
    val success: Boolean
)
