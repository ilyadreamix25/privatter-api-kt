package com.privatter.api.v1.session.enums

enum class SessionFromTokenResult {
    INVALID_TOKEN,
    INVALID_HASH,
    INVALID_VERSION,
    EXPIRED,
    OK
}
