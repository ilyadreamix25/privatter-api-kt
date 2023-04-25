package com.privatter.api.v1

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PrivatterApplication

fun main(vararg args: String) {
    runApplication<PrivatterApplication>(*args)
}
