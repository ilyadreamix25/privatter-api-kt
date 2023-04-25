package com.privatter.api.v1.verification.entity

import com.privatter.api.v1.user.entity.UserEntity
import com.privatter.api.v1.verification.enums.VerificationAction
import jakarta.persistence.*
import java.util.UUID

// @Open
@Entity
@Table(name = "verifications")
data class VerificationEntity(
    @Id
    @Column(name = "id", unique = true)
    var id: String = UUID.randomUUID().toString(),

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "action")
    var action: VerificationAction,

    @Column(name = "created_at")
    var createdAt: Long = System.currentTimeMillis(),

    @Column(name = "expiration_time")
    var expirationTime: Long,

    @Column(name = "secret")
    var secret: String,

    @ManyToOne
    @JoinColumn(name = "user_id")
    var user: UserEntity
)
