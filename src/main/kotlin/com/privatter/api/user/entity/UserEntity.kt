package com.privatter.api.user.entity

import jakarta.persistence.Column
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "users")
class UserEntity(
    @Id
    @Column(name = "id", unique = true)
    var id: String = UUID.randomUUID().toString(),

    @Embedded
    var auth: UserEntityAuth,

    @Embedded
    var profile: UserEntityProfile,

    @Embedded
    var metadata: UserEntityMetadata = UserEntityMetadata()
)
