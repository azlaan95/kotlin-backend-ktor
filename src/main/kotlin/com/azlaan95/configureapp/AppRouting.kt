package com.azlaan95.configureapp

import com.azlaan95.AppDependencyInject
import com.azlaan95.approuting.loginRoute
import com.azlaan95.approuting.registerRoute
import com.azlaan95.approuting.tokensRoute
import com.azlaan95.approuting.usersRoute
import com.azlaan95.models.AppResponse
import com.azlaan95.models.User
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.runBlocking

fun Application.configureRouting() {
    routing {
        route("/") {
            get {
                runBlocking {
                    var users: List<User> = AppDependencyInject.userDaoProvider().getUsers()
                    val response = AppResponse(message = "Here are list of users", appCode = 200, data = users)
                    call.respond(response)
                }
            }
        }
        registerRoute(userDao = AppDependencyInject.userDaoProvider())
        authenticate("auth-basic") {
            loginRoute(tokensDao = AppDependencyInject.tokenDaoProvider())
        }
        authenticate("auth-bearer") {
            usersRoute(usersDao = AppDependencyInject.userDaoProvider())
            tokensRoute(tokensDao = AppDependencyInject.tokenDaoProvider())
        }
    }
}