package com.privatter.api.utility

class ValidationException(override val message: String? = "Validation error") : IllegalArgumentException()

fun validate(
    requirement: Boolean,
    fieldName: String? = null
) {
    if (!requirement) {
        val formattedMessage = if (fieldName != null) "Invalid field: $fieldName" else null
        throw ValidationException(formattedMessage)
    }
}
