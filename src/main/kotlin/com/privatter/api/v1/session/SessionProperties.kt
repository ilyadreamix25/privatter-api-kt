package com.privatter.api.v1.session

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties("privatter.session")
class SessionProperties {
    lateinit var secret: String
    lateinit var algorithm: String
}
