package com.privatter.api.v1.utility

import jakarta.mail.internet.InternetAddress
import java.util.Base64

fun String.isEmail() =
    try {
        InternetAddress(this).validate()
        true
    } catch (_: Exception) {
        false
    }

fun String.toBase64(): String = Base64.getEncoder().encodeToString(this.toByteArray())

fun String.fromBase64ToString(): String = String(Base64.getDecoder().decode(this))
