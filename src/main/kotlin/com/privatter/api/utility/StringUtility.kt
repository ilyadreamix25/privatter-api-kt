package com.privatter.api.utility

import jakarta.mail.internet.InternetAddress
import java.util.Base64
import kotlin.reflect.KClass

fun parseEnumError(parent: KClass<*>, child: Any) = "${parent.simpleName}.$child"

fun String.isEmail() =
    try {
        InternetAddress(this).validate()
        true
    } catch (_: Exception) {
        false
    }

fun String.toBase64(): String = Base64.getEncoder().encodeToString(this.toByteArray())

fun String.fromBase64ToString(): String = String(Base64.getDecoder().decode(this))
