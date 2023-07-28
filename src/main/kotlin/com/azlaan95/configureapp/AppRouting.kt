package com.azlaan95.configureapp

import com.azlaan95.AppDependencyInject
import com.azlaan95.approuting.loginRoute
import com.azlaan95.approuting.registerRoute
import com.azlaan95.approuting.tokensRoute
import com.azlaan95.approuting.usersRoute
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
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