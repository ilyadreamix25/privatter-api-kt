package com.privatter.api.v1.user.entity

import com.privatter.api.v1.verification.entity.VerificationEntity
import jakarta.persistence.*
import java.util.UUID

@Entity
@Table(name = "users")
data class UserEntity(
    @Id
    @Column(name = "id", unique = true)
    var id: String = UUID.randomUUID().toString(),

    @Embedded
    var auth: UserEntityAuth = UserEntityAuth(),

    @Embedded
    var profile: UserEntityProfile = UserEntityProfile(),

    @Embedded
    var metadata: UserEntityMetadata = UserEntityMetadata(),

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    var verifications: List<VerificationEntity> = listOf()
)
