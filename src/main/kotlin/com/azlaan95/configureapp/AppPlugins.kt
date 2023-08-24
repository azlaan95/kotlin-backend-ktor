package com.azlaan95.configureapp

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*

fun Application.installPlugins() {
    install(CallLogging)
    install(Authentication)
    install(ContentNegotiation) {
        json()
    }

}