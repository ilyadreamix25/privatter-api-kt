package com.privatter.api.user.entity

import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
class UserEntityAuth(
    @Column(name = "auth_key", unique = true)
    var key: String,

    @Column(name = "auth_value")
    var value: String
)
