package com.privatter.api.session.enums

enum class SessionFromTokenResult {
    INVALID_TOKEN,
    INVALID_HASH,
    INVALID_VERSION,
    EXPIRED,
    OK
}
