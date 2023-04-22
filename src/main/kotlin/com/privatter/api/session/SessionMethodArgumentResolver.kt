package com.privatter.api.session

import com.privatter.api.session.entity.SessionEntity
import com.privatter.api.session.enums.SessionFromTokenResult
import com.privatter.api.session.exception.SessionException
import org.springframework.core.MethodParameter
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

private const val SESSION_TOKEN_HEADER = "Privatter-Session-Token"

class SessionMethodArgumentResolver(private val service: SessionService) : HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter) =
        parameter.parameterType == SessionEntity::class.java

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Any {
        val sessionHeader = webRequest.getHeader(SESSION_TOKEN_HEADER)
            ?: throw SessionException(reLogin = false)

        val (result, session) = service.fromToken(sessionHeader)
        when (result) {
            SessionFromTokenResult.OK -> return session!!
            SessionFromTokenResult.EXPIRED -> throw SessionException(reLogin = true)
            else -> throw SessionException(reLogin = false)
        }
    }
}
