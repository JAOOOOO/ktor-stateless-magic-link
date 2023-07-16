package com.jaooooo.plugins

import com.jaooooo.services.TokenService
import io.ktor.server.html.*
import kotlinx.html.*
import kotlinx.css.*
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.Database

fun Application.configureTemplating() {


    routing {

        get("/styles.css") {
            call.respondCss {
                body {
                    backgroundColor = Color.whiteSmoke
                    margin(0.px)
                    padding(20.px)
                }
                rule("h1.page-title") {
                    color = Color.black

                }

                rule("input") {
                    flexDirection = FlexDirection.row
                    display = Display.flex
                    margin(10.px)
                }

                rule("input[type=text]") {
                    padding(10.px)
                    width = 30.pct
                    fontSize = 24.pct
                }
                rule("input[type=submit]") {
                    padding(10.px)
                    margin(10.px)
                    fontSize = 20.pct

                }
            }
        }
    }
}

suspend inline fun ApplicationCall.respondCss(builder: CSSBuilder.() -> Unit) {
    this.respondText(CSSBuilder().apply(builder).toString(), ContentType.Text.CSS)
}

