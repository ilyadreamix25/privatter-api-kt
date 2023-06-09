package com.privatter.api.v1.recaptcha

import com.privatter.api.v1.recaptcha.models.ReCaptchaTokenVerificationResultModel
import com.privatter.api.v1.server.ServerProperties
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class ReCaptchaService(
    private val properties: ReCaptchaProperties,
    private val serverProperties: ServerProperties
) {
    private val json = Json { ignoreUnknownKeys = true }

    fun siteVerifyToken(token: String): Boolean {
        if (serverProperties.debug)
            return true

        val restTemplate = RestTemplate()
        val response = restTemplate.getForEntity(
            properties.remote,
            String::class.java,
            properties.secret,
            token
        )

        if (response.statusCode != HttpStatus.OK || response.body == null)
            return false

        return json.decodeFromString<ReCaptchaTokenVerificationResultModel>(response.body!!).success
    }
}
