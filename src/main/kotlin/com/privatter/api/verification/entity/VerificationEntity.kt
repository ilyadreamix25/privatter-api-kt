package com.privatter.api.verification.entity

import com.privatter.api.verification.enums.VerificationAction
import jakarta.persistence.*
import java.util.UUID

@Entity
@Table(name = "verifications")
class VerificationEntity(
    @Id
    @Column(name = "id", unique = true)
    var id: String = UUID.randomUUID().toString(),

    @Column(name = "user_id")
    var userId: String,

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "action")
    var action: VerificationAction,

    @Column(name = "created_at")
    var createdAt: Long = System.currentTimeMillis(),

    @Column(name = "expiration_time")
    var expirationTime: Long,

    @Column(name = "secret")
    var secret: String
)
