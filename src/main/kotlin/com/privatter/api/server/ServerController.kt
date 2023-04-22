package com.privatter.api.server

import com.privatter.api.core.PrivatterResponseResource
import com.privatter.api.core.model.PrivatterResponseEntity
import com.privatter.api.server.model.ServerInformationModel
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("server")
class ServerController(private val properties: ServerProperties) {
    @GetMapping("information")
    fun information(): PrivatterResponseEntity<ServerInformationModel> = PrivatterResponseEntity.ok()
        .body(
            PrivatterResponseResource.parseOk(
                ServerInformationModel(
                    version = properties.version,
                    debug = properties.debug
                )
            )
        )
}
