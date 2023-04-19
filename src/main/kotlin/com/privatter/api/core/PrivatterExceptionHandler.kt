package com.privatter.api.core

import com.privatter.api.core.model.PrivatterEmptyResponseEntity
import com.privatter.api.utility.beautify
import org.springframework.http.HttpStatus
import org.springframework.http.converter.HttpMessageNotReadableException
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
        PrivatterResponseResource.Model.INVALID_SERVICE

    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    fun handleHttpRequestMethodNotSupportedException() =
        PrivatterResponseResource.Model.INVALID_METHOD

    @ExceptionHandler(HttpMessageNotReadableException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleHttpMessageNotReadableException(): PrivatterEmptyResponseEntity = PrivatterEmptyResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(PrivatterResponseResource.Model.INVALID_REQUEST)

    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleInternalServerException(exception: Exception) = PrivatterResponseResource.parseError(
        resource = PrivatterResponseResource.SERVER_ERROR,
        errorMessage = exception.message,
        errorStackTrace = exception.beautify(true).take(4)
    )
}
