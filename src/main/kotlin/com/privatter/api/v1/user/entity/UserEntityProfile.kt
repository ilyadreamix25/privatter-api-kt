package com.privatter.api.v1.user.entity

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.Lob

@Embeddable
data class UserEntityProfile(
    @Column(name = "profile_nickname", unique = true)
    var nickname: String? = null,

    @Lob
    @Column(name = "profile_description")
    var description: String? = null,

    @Column(name = "profile_icon_url")
    var iconUrl: String? = null
)