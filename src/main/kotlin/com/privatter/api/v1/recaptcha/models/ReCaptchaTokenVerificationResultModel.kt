package com.privatter.api.v1.recaptcha.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReCaptchaTokenVerificationResultModel(
    @SerialName("success")
    val success: Boolean
)
