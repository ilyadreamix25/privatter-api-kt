package com.privatter.api.utility

class ValidationException(override val message: String? = "Validation error") : IllegalArgumentException()

fun validate(
    requirement: Boolean,
    message: String? = null
) {
    if (!requirement) {
        val formattedMessage = if (message != null) "Invalid field: $message" else null
        throw ValidationException(formattedMessage)
    }
}
