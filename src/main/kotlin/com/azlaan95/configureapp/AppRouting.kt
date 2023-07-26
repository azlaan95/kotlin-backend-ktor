package com.azlaan95.configureapp

import com.azlaan95.approuting.authenticateRoute
import com.azlaan95.approuting.registerRoute
import com.azlaan95.approuting.tokensRoute
import com.azlaan95.approuting.usersRoute
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        registerRoute()
        authenticate("auth-basic") {
            authenticateRoute()
        }
        authenticate("auth-bearer") {
            usersRoute()
            tokensRoute()
        }
    }
}