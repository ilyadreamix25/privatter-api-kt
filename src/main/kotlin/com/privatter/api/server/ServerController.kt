package com.privatter.api.server

import com.privatter.api.core.PrivatterResponseResource
import com.privatter.api.core.model.PrivatterResponseEntity
import com.privatter.api.server.model.ServerStatusModel
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("server")
class ServerController(private val properties: ServerProperties) {
    @GetMapping("status")
    fun status(): PrivatterResponseEntity<ServerStatusModel> = PrivatterResponseEntity.ok()
        .body(
            PrivatterResponseResource.parseOk(
                ServerStatusModel(
                    version = properties.version,
                    debug = properties.debug
                )
            )
        )
}
