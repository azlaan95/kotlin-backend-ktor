package com.azlaan95.approuting

import com.azlaan95.database.daofacade.tokens.JwtTokensDao
import com.azlaan95.database.daofacade.tokens.JwtTokensDaoImpl
import com.azlaan95.models.AppResponse
import com.azlaan95.providers.jwt.JWTProvider
import com.azlaan95.util.AppGson
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.runBlocking

fun Route.authenticateRoute() {
    route("/authenticate") {
        get("/login") {
            val userId: String = call.principal<UserIdPrincipal>()?.name!!
            val userJwtToken = JWTProvider.generateJwtToken(userId);
            val dao: JwtTokensDao = JwtTokensDaoImpl();
            runBlocking {
                dao.addToken(userJwtToken)
                val response = AppResponse(
                    message = "You are loggedin",
                    appCode = 200,
                    data = userJwtToken
                )
                call.respond(AppGson.gson.toJson(response))
            }
        }
    }
}