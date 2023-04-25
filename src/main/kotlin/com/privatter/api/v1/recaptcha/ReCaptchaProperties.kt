package com.privatter.api.v1.recaptcha

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "privatter.google.recaptcha")
class ReCaptchaProperties {
    lateinit var secret: String
    lateinit var remote: String
}
