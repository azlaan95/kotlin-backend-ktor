package com.azlaan95.configureapp

import com.azlaan95.database.AppStore
import com.azlaan95.database.daofacade.user.UsersDao
import com.azlaan95.database.daofacade.user.UsersDaoImpl
import com.azlaan95.models.AppResponse
import com.azlaan95.models.User
import com.azlaan95.providers.jwt.JWTProvider
import com.azlaan95.util.AppGson
import io.ktor.server.auth.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import kotlinx.coroutines.runBlocking


fun Application.configureAuthorization() {
    authentication {
        basic("auth-basic") {
            validate { credentials ->
                val dao: UsersDao = UsersDaoImpl()
                runBlocking {
                    var loggedInUser: User? = dao.authUser(credentials.name, credentials.password)
                    if (loggedInUser != null) {
                        UserIdPrincipal(loggedInUser.email)
                    } else {
                        null
                    }
                }
            }
        }

        bearer("auth-bearer") {
            authenticate { tokenCredential ->
                val userId = JWTProvider.isValidToken(tokenCredential.token)
                val dao: UsersDao = UsersDaoImpl()
                runBlocking {
                    var user: User? = dao.getUserByToken(tokenCredential.token)
                    if (userId!=null && user?.email == userId) {
                        UserIdPrincipal(userId)
                    } else {
                        null
                    }
                }

            }
        }

    }
}
