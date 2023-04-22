package com.privatter.api.utility

import java.util.Base64

fun ByteArray.toBase64(): String = Base64.getEncoder().encodeToString(this)

fun ByteArray.toSafeBase64(): String = Base64.getUrlEncoder().encodeToString(this)

fun String.fromBase64ToByteArray(): ByteArray = Base64.getDecoder().decode(this)
