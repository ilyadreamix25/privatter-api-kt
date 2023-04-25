package com.privatter.api.v1.core

import com.privatter.api.v1.core.model.PrivatterEmptyResponseModel
import com.privatter.api.v1.core.model.PrivatterResponseModel
import com.privatter.api.v1.session.exception.SessionException
import com.privatter.api.v1.utility.ValidationException
import com.privatter.api.v1.utility.beautifyStackTrace
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
        PrivatterResponseResource.INVALID_SERVICE.asErrorModel

    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    fun handleHttpRequestMethodNotSupportedException() =
        PrivatterResponseResource.INVALID_METHOD.asErrorModel

    @ExceptionHandler(HttpMessageNotReadableException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleHttpMessageNotReadableException() =
        PrivatterResponseResource.INVALID_REQUEST.asErrorModel

    @ExceptionHandler(ValidationException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleValidationException(exception: ValidationException) = PrivatterResponseResource.parseError(
        resource = PrivatterResponseResource.INVALID_REQUEST,
        errorInformation = PrivatterResponseModel.ErrorInformation(message = exception.message)
    )

    @ExceptionHandler(SessionException::class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    fun handleSessionException(exception: SessionException) =
        if (exception.reLogin) PrivatterResponseResource.USER_SESSION_EXPIRED.asErrorModel
        else PrivatterResponseResource.INVALID_SESSION.asErrorModel

    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleInternalServerException(exception: Exception): PrivatterEmptyResponseModel {
        LoggerFactory
            .getLogger(PrivatterExceptionHandler::class.java)
            .error(exception.message, exception)

        return PrivatterResponseResource.parseError(
            resource = PrivatterResponseResource.SERVER_ERROR,
            errorInformation = PrivatterResponseModel.ErrorInformation(
                message = exception.message,
                stackTrace = exception.beautifyStackTrace(true).take(4)
            )
        )
    }
}
