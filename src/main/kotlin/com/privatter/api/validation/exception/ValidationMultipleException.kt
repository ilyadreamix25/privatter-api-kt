package com.privatter.api.validation.exception

class ValidationMultipleException(
    val validationExceptions: List<ValidationException>
) : Exception("Multiple validation error")
