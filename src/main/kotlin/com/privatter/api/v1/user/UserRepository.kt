package com.privatter.api.v1.user

import com.privatter.api.v1.user.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface UserRepository : JpaRepository<UserEntity, String> {
    @Query("SELECT * FROM users x WHERE x.id = ?1 OR (x.auth_key = ?2 AND x.profile_nickname = ?3)", nativeQuery = true)
    fun find(id: String? = null, authKey: String? = null, profileNickname: String? = null): UserEntity?

    @Query("SELECT * FROM users x WHERE x.auth_key = ?1", nativeQuery = true)
    fun findByAuthKey(authKey: String): UserEntity?

    @Query("SELECT * FROM users x WHERE x.profile_nickname = ?1", nativeQuery = true)
    fun findByProfileNickname(profileNickname: String): UserEntity?
}
