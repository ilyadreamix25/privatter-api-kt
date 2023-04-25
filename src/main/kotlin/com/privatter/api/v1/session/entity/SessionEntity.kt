package com.privatter.api.v1.session.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "sessions")
class SessionEntity(
    @Id
    @Column(name = "id", unique = true)
    val id: String = UUID.randomUUID().toString(),

    @Column(name = "user_id")
    val userId: String,

    @Column(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),

    @Column(name = "expiration_time")
    val expirationTime: Long,

    @Column(name = "ip")
    val ip: String,

    @Column(name = "version")
    val version: String
)
