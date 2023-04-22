package com.privatter.api.user.entity

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.Lob

@Embeddable
class UserEntityProfile(
    @Column(name = "profile_nickname", unique = true)
    var nickname: String,

    @Lob
    @Column(name = "profile_description")
    var description: String? = null,

    @Column(name = "profile_icon_url")
    var iconUrl: String? = null
)