package com.privatter.api.v1.user.entity

import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
data class UserEntityAuth(
    @Column(name = "auth_key", unique = true)
    var key: String? = null,

    @Column(name = "auth_value")
    var value: String? = null,

    @Column(name = "auth_verified")
    var verified: Boolean = false,

    @Column(name = "auth_verified_at")
    var verifiedAt: Long = 0,

    @Column(name = "auth_ips")
    var ips: List<String>? = null
)
