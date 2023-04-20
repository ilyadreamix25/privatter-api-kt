package com.privatter.api.utility

import com.privatter.api.validation.*
import com.privatter.api.validation.exception.ValidationException
import com.privatter.api.validation.exception.ValidationMultipleException

fun validateFromRequirement(
    requirement: Boolean,
    message: String? = null,
    throwException: Boolean = true
): ValidationException? {
    if (!requirement && throwException)
        throw ValidationException(message)

    return if (!requirement)
        ValidationException(message)
    else null
}

fun <T : Any> validateFromContext(context: T) {
    val validationProperties = context::class.findValidationProperties()
    val validationExceptions = validationProperties.getValidationExceptions(context)
    if (validationExceptions.isNotEmpty())
        throw ValidationMultipleException(validationExceptions)
}
