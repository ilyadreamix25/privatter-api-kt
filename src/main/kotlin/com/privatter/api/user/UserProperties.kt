package com.privatter.api.user

import com.privatter.api.user.enums.UserAuthMethod
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "privatter.user")
class UserProperties {
    lateinit var authMethods: List<UserAuthMethod>
    lateinit var secret: String
    lateinit var algorithm: String
}