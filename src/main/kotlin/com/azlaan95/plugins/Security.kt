package com.azlaan95.plugins

import com.azlaan95.database.AppStore
import com.azlaan95.providers.jwt.JWTProvider
import io.ktor.server.auth.*
import io.ktor.server.application.*


fun Application.configureSecurity() {
    install(Authentication) {
        basic("auth-basic") {
            validate { credentials ->
                val loggedInUser = AppStore.users.find { user ->
                    user.email == credentials.name && user.password == credentials.password
                }
                if (loggedInUser != null) {
                    UserIdPrincipal(loggedInUser.email)
                } else {
                    null
                }
            }
        }

        bearer("auth-bearer") {
            authenticate { tokenCredential ->
                val userId = JWTProvider.isValidToken(tokenCredential.token)
                if (userId != null && AppStore.compareToken(userId, tokenCredential.token)) {
                    UserIdPrincipal(userId)
                } else {
                    null
                }
            }
        }

    }
}
