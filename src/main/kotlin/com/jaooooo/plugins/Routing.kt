package com.jaooooo.plugins

import com.jaooooo.services.TokenService
import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.request.*
import io.ktor.server.util.*
import kotlinx.html.*
import org.jetbrains.exposed.sql.Database

fun Application.configureRouting() {
    val secret = environment.config.property("jwt.secret").getString()
    val issuer = environment.config.property("jwt.issuer").getString()
    val audience = environment.config.property("jwt.audience").getString()
    val myRealm = environment.config.property("jwt.realm").getString()

    val tokenService = TokenService(secret, issuer, audience, myRealm)



    val database = Database.connect(
        url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
        user = "root",
        driver = "org.h2.Driver",
        password = ""
    )
    val userService = UserService(database)


    routing {
        get("/") {
            call.respondHtml {
                head {
                    link(rel = "stylesheet", href = "/styles.css", type = "text/css")
                }
                body {
                    h1(classes = "page-title") {
                        +"Hello from Ktor! Type your email to get started"
                    }
                    form(action = "/login", method = FormMethod.post) {
                        acceptCharset = "utf-8"
                        textInput { name = "email" }
                        submitInput { value = "Send Magic Link" }
                    }
                }
            }
        }

        post("/login") {
            val formParameters = call.receiveParameters()
            val email = formParameters.getOrFail("email")

            val user = userService.getOrCreate(email)

            val token = tokenService.generateToken(mapOf("email" to user.email, "id" to user.id.toString()))

            //val redirectPath = "/validate?token=$token&email=$email"
            val baseUrl = call.request.host()
            val redirectUrl = "http://$baseUrl:8080/validate?token=$token&email=$email"

            call.respondHtml {
                head {
                    link(rel = "stylesheet", href = "/styles.css", type = "text/css")
                }
                body {

                    h2 {
                        +"Magic link sent to $email with token"
                    }

                        getButton() {
                            onClick = "window.location.href='$redirectUrl'"
                            +"Click here to validate"
                        }

                }
            }
        }

        get("validate") {
            val token = call.parameters.getOrFail("token")
            val email = call.parameters.getOrFail("email")

            val user = userService.readByEmail(email) ?: throw Exception("User not found")



            val isValid = tokenService.validateToken(token, mapOf("email" to email , "id" to user.id.toString()))

            if (isValid) {
                call.respondHtml {
                    head {
                        link(rel = "stylesheet", href = "/styles.css", type = "text/css")
                    }
                    body {
                        h2 {
                            +"Welcome $email"
                        }
                    }
                }
            } else {
                call.respondHtml {
                    head {
                        link(rel = "stylesheet", href = "/styles.css", type = "text/css")
                    }
                    body {
                        h2 {
                            +"Invalid token"
                        }
                    }
                }
            }
        }

    }
}
