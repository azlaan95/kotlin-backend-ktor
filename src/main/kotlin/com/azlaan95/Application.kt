package com.azlaan95

import com.azlaan95.configureapp.configureRouting
import com.azlaan95.configureapp.configureAuthorization
import com.azlaan95.configureapp.installPlugins
import com.azlaan95.database.DatabaseFactory
import io.ktor.server.application.*

fun main(args: Array<String>): Unit =
    io.ktor.server.cio.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    DatabaseFactory.init(environment.config)
    installPlugins()
    configureAuthorization(userDao = AppDependencyInject.userDaoProvider())
    configureRouting()
}
