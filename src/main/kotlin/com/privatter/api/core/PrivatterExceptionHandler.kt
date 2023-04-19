package com.privatter.api.core

import com.privatter.api.utility.beautify
import org.springframework.http.HttpStatus
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.NoHandlerFoundException
import org.springframework.web.servlet.config.annotation.EnableWebMvc

@EnableWebMvc
@RestControllerAdvice
class PrivatterExceptionHandler {
    @ExceptionHandler(NoHandlerFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleNoHandlerFoundException() =
        PrivatterResponseResource.parseError(PrivatterResponseResource.INVALID_SERVICE)

    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    fun handleHttpRequestMethodNotSupportedException() =
        PrivatterResponseResource.parseError(PrivatterResponseResource.INVALID_METHOD)

    @ExceptionHandler(Exception::class)
    fun handleInternalServerException(exception: Exception) =
        PrivatterResponseResource.parseError(
            resource = PrivatterResponseResource.SERVER_ERROR,
            errorStackTrace = exception.beautify(true).take(4),
            errorMessage = exception.message
        )
}
