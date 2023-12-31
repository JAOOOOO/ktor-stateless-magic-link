package com.jaooooo.plugins

import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.plugins.openapi.*
import io.ktor.server.routing.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.application.*

fun Application.configureHTTP() {
    install(DefaultHeaders) {
        header("X-Engine", "Ktor") // will send this header with each response
    }
    routing {
        openAPI(path = "openapi")
    }
    routing {
        swaggerUI(path = "openapi")
    }
}
