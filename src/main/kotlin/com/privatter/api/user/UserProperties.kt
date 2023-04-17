package com.privatter.api.user

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "privatter.user")
class UserProperties {
    lateinit var authMethods: List<String>
}