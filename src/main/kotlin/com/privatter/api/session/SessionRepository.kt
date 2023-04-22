package com.privatter.api.session

import com.privatter.api.session.entity.SessionEntity
import org.springframework.data.jpa.repository.JpaRepository

interface SessionRepository : JpaRepository<SessionEntity, String>
