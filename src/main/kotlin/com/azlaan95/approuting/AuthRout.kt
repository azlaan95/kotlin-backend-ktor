package com.azlaan95.approuting

import com.azlaan95.database.AppStore
import com.azlaan95.models.AppResponse
import com.azlaan95.providers.jwt.JWTProvider
import com.azlaan95.util.AppGson
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.authenticateRoute() {
    route("/authenticate") {
        get("/login") {
            val userEmail: String = call.principal<UserIdPrincipal>()?.name!!
            val userJwtToken = JWTProvider.generateJwtToken(userEmail);
            AppStore.addToken(userEmail, userJwtToken)
            val response = AppResponse(
                message = "You are loggedin",
                appCode = 200,
                data = userJwtToken
            )
            call.respond(AppGson.gson.toJson(response))
        }
    }
}