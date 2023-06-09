package com.privatter.api.v1.user.entity

import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
data class UserEntityMetadata(
    @Column(name = "metadata_signed_up_at")
    var signedUpAt: Long = System.currentTimeMillis(),

    @Column(name = "metadata_last_signed_in_at")
    var lastSignedInAt: Long = 0
)
