package com.privatter.api.v1.core

import com.privatter.api.v1.session.SessionMethodArgumentResolver
import com.privatter.api.v1.session.SessionService
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@EnableWebMvc
class PrivatterWebMvcConfiguration(private val sessionService: SessionService) : WebMvcConfigurer {
    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(SessionMethodArgumentResolver(sessionService))
    }
}
