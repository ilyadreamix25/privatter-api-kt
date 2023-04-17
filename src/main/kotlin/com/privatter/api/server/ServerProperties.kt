package com.privatter.api.server

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "privatter.server")
class ServerProperties {
    lateinit var host: String
    lateinit var port: String
    lateinit var version: String
}
