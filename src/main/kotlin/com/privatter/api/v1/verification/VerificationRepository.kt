package com.privatter.api.v1.verification

import com.privatter.api.v1.verification.entity.VerificationEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface VerificationRepository : JpaRepository<VerificationEntity, String> {
    @Query("SELECT * FROM verifications x WHERE x.user_id = ?1 AND x.action = ?2", nativeQuery = true)
    fun findByUserId(userId: String, action: Int): VerificationEntity?
}
