package com.azlaan95.configureapp

import com.azlaan95.database.daofacade.user.UsersDao
import com.azlaan95.models.User
import com.azlaan95.providers.jwt.JWTProvider
import io.ktor.server.application.*
import io.ktor.server.auth.*
import kotlinx.coroutines.runBlocking


fun Application.configureAuthorization(userDao: UsersDao) {
    authentication {
        basic("auth-basic") {
            validate { credentials ->
                runBlocking {
                    var loggedInUser: User? = userDao.authUser(credentials.name, credentials.password)
                    if (loggedInUser != null) {
                        UserIdPrincipal(loggedInUser.id.toString())
                    } else {
                        null
                    }
                }
            }
        }

        bearer("auth-bearer") {
            authenticate { tokenCredential ->
                val userId = JWTProvider.isValidToken(tokenCredential.token)
                print(request.headers["id"])
                if (userId != null) {
                    UserIdPrincipal(userId)
                } else {
                    null
                }
            }
        }

    }
}
