package com.azlaan95.approuting

import com.azlaan95.database.daofacade.tokens.JwtTokensDao
import com.azlaan95.models.AppError
import com.azlaan95.models.AppResponse
import com.azlaan95.models.User
import com.azlaan95.providers.jwt.JwtToken
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.runBlocking

fun Route.tokensRoute(tokensDao: JwtTokensDao) {
    route("/tokens") {
        get("/all") {
            val idHeader = call.request.headers["id"]
            val userEmail: String = call.principal<UserIdPrincipal>()?.name!!
            if (idHeader == userEmail) {
                runBlocking {
                    var tokens: List<JwtToken> = tokensDao.getTokens()
                    val response = AppResponse(message = "Here are list of Tokens", appCode = 200, data = tokens)
                    call.respond(response)
                }
            } else {
                val response = AppResponse<User>(
                    appCode = 400,
                    error = AppError(errorMessage = "You are not authorized, Try login again")
                )
                call.respond(status = HttpStatusCode.Unauthorized, message = response)
            }

        }
        get("/{id}") {
            val idHeader = call.request.headers["id"]
            val userEmail: String = call.principal<UserIdPrincipal>()?.name!!
            if (idHeader == userEmail) {
                val id = call.parameters["id"] ?: "0"
                runBlocking {
                    var tokens: List<JwtToken> = tokensDao.getTokenById(Integer.parseInt(id))
                    val response = AppResponse(message = "Token Found", appCode = 200, data = tokens)
                    call.respond(response)
                }
            } else {
                val response = AppResponse<User>(
                    appCode = 400,
                    error = AppError(errorMessage = "You are not authorized, Try login again")
                )
                call.respond(status = HttpStatusCode.Unauthorized, message = response)
            }
        }
    }
}