package com.privatter.api.v1.user.enums

enum class UserAuthMethod(private val methodName: String) {
    EMAIL("email"),
    GOOGLE_OAUTH("google-oauth");

    companion object {
        @JvmStatic
        fun of(methodName: String) =
            UserAuthMethod.values().find { value ->
                methodName.lowercase() == value.methodName
            }
    }
}
