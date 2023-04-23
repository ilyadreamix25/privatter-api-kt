package com.privatter.api.verification.enums

enum class VerificationAction(val actionName: String) {
    USER_SIGN_UP("sign-up"),
    USER_SIGN_IN("sign-in"),
    USER_DELETE("delete");

    companion object {
        @JvmStatic
        fun of(actionName: String) =
            VerificationAction.values().find {  value ->
                actionName.lowercase() == value.actionName
            }
    }
}
