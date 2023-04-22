package com.privatter.api.session

import com.privatter.api.server.ServerProperties
import com.privatter.api.session.entity.SessionEntity
import com.privatter.api.session.enums.SessionFromTokenResult
import com.privatter.api.session.model.SessionHeaderModel
import com.privatter.api.session.model.SessionModel
import com.privatter.api.utility.fromBase64ToString
import com.privatter.api.utility.toBase64
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.springframework.stereotype.Service
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import kotlin.jvm.optionals.getOrNull

@Service
class SessionService(
    private val properties: SessionProperties,
    private val repository: SessionRepository,
    private val serverProperties: ServerProperties
) {
    private val json = Json { ignoreUnknownKeys = true }

    fun create(userId: String, ip: String, expirationTime: Long): Pair<SessionModel, String> {
        val sessionEntity = repository.save(
            SessionEntity(
                userId = userId,
                ip = ip,
                expirationTime = expirationTime,
                version = serverProperties.version
            )
        )
        val sessionModel = SessionModel(
            id = sessionEntity.id,
            userId = sessionEntity.userId
        )
        val header = SessionHeaderModel(
            algorithm = properties.algorithm,
            version = serverProperties.version
        )
        return sessionModel to generateToken(header, sessionModel)
    }

    fun fromToken(token: String): Pair<SessionFromTokenResult, SessionEntity?> {
        try {
            val (encodedHeader, encodedSession, hash) = token.split(".")

            val jsonHeader = encodedHeader.fromBase64ToString()
            val jsonSession = encodedSession.fromBase64ToString()

            if (generateHash(jsonHeader, jsonSession) != hash)
                return SessionFromTokenResult.INVALID_HASH to null

            val clientHeader = json.decodeFromString<SessionHeaderModel>(jsonHeader)
            val clientSession = json.decodeFromString<SessionModel>(jsonSession)

            val serverSession = repository.findById(clientSession.id).getOrNull()
                ?: return SessionFromTokenResult.INVALID_TOKEN to null

            if (serverSession.version != clientHeader.version || serverProperties.version != clientHeader.version)
                return SessionFromTokenResult.INVALID_VERSION to null

            if (System.currentTimeMillis() > serverSession.createdAt + serverSession.expirationTime)
                return SessionFromTokenResult.EXPIRED to null

            return SessionFromTokenResult.OK to serverSession
        } catch (_: Exception) {
            return SessionFromTokenResult.INVALID_TOKEN to null
        }
    }

    private fun generateToken(header: SessionHeaderModel, sessionModel: SessionModel): String {
        val jsonHeader = json.encodeToString(header)
        val jsonModel = json.encodeToString(sessionModel)

        return "${jsonHeader.toBase64()}." +
            "${jsonModel.toBase64()}." +
            generateHash(jsonHeader, jsonModel)
    }

    private fun generateHash(header: String, session: String): String {
        val secretKeySpec = SecretKeySpec(properties.secret.toByteArray(), properties.algorithm)
        return Mac.getInstance(properties.algorithm)
            .apply {
                init(secretKeySpec)
            }
            .doFinal((header + session).toByteArray())
            .toBase64()
    }
}
