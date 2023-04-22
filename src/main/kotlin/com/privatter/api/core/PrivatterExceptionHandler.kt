package com.privatter.api.core

import com.privatter.api.core.model.PrivatterEmptyResponseModel
import com.privatter.api.session.exception.SessionException
import com.privatter.api.utility.ValidationException
import com.privatter.api.utility.beautifyStackTrace
import org.slf4j.LoggerFactory
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
    fun handleHttpMessageNotReadableException() =
        PrivatterResponseResource.Model.INVALID_REQUEST

    @ExceptionHandler(ValidationException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleValidationException(exception: ValidationException) = PrivatterResponseResource.parseError(
        resource = PrivatterResponseResource.INVALID_REQUEST,
        errorMessage = exception.message
    )

    @ExceptionHandler(SessionException::class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    fun handleSessionException(exception: SessionException) =
        if (exception.reLogin) PrivatterResponseResource.Model.SESSION_EXPIRED
        else PrivatterResponseResource.Model.INVALID_SESSION

    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleInternalServerException(exception: Exception): PrivatterEmptyResponseModel {
        LoggerFactory
            .getLogger(PrivatterExceptionHandler::class.java)
            .error(exception.message, exception)

        return PrivatterResponseResource.parseError(
            resource = PrivatterResponseResource.SERVER_ERROR,
            errorMessage = exception.message,
            errorInformation = exception.beautifyStackTrace(true).take(4)
        )
    }
}
