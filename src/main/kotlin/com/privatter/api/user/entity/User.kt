package com.privatter.api.user.entity

import jakarta.persistence.Column
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "user")
class User(
    @Id
    @Column(name = "id", unique = true)
    var id: String,

    @Embedded
    var auth: UserAuth,

    @Embedded
    var profile: UserProfile,

    @Embedded
    var metadata: UserMetadata
)
