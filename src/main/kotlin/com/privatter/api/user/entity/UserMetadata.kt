package com.privatter.api.user.entity

import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
class UserMetadata(
    @Column(name = "metadata_verified")
    var verified: Boolean = false,

    @Column(name = "metadata_verified_at")
    var verifiedAt: Long = 0,

    @Column(name = "metadata_signed_up_at")
    var signedUpAt: Long = 0,

    @Column(name = "metadata_last_signed_in_at")
    var lastSignedInAt: Long = 0
)
