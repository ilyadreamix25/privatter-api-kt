package com.privatter.api.verification

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "privatter.verification")
class VerificationProperties {
    lateinit var secret: String
    lateinit var algorithm: String
}
