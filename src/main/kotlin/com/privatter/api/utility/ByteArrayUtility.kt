package com.privatter.api.utility

fun ByteArray.toHexString() = joinToString("") { "%02x".format(it) }
