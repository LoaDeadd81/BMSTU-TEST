package com.example.plugins

import api.routes.auth
import api.routes.ingredients
import api.routes.recipes
import api.routes.users
import io.ktor.server.application.*
import io.ktor.server.resources.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    install(Resources)
    routing {
        route("/api/v1") {
            users()
            ingredients()
            recipes()
            auth()
        }
    }
}
