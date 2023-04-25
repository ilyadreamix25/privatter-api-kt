package com.privatter.api.v1.session

import com.privatter.api.v1.session.entity.SessionEntity
import org.springframework.data.jpa.repository.JpaRepository

interface SessionRepository : JpaRepository<SessionEntity, String>
