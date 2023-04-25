package com.privatter.api.v1.user.model

import com.privatter.api.v1.utility.validate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserVerificationTokenModel(
    @SerialName("id")
    val id: String,

    @SerialName("secret")
    val secret: String
) {
    init {
        validate(id.isNotEmpty(), ::id.name)
        validate(secret.isNotEmpty(), ::secret.name)
    }
}
