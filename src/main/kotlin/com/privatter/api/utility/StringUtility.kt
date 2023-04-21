package com.privatter.api.utility

import jakarta.mail.internet.InternetAddress
import kotlin.reflect.KClass

fun parseEnumError(parent: KClass<*>, child: Any) = "${parent.simpleName}.$child"

fun String.isEmail() =
    try {
        InternetAddress(this).validate()
        true
    } catch (_: Exception) {
        false
    }
