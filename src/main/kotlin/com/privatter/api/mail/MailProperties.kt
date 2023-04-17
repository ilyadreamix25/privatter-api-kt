package com.privatter.api.mail

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "privatter.google.mail")
class MailProperties {
    lateinit var username: String
    lateinit var password: String
}
