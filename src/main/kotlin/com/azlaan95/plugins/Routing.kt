package com.azlaan95.plugins

import com.azlaan95.database.AppStore
import com.azlaan95.models.AppError
import com.azlaan95.models.AppResponse
import com.azlaan95.models.User
import com.azlaan95.providers.jwt.JWTProvider
import com.azlaan95.util.AppGson
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json

fun Application.configureRouting() {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
        })
    }
    routing {
        authenticate("auth-basic") {
            get("/login") {
                val userEmail: String = call.principal<UserIdPrincipal>()?.name!!
                val userJwtToken = JWTProvider.generateJwtToken(userEmail);
                AppStore.addToken(userEmail, userJwtToken)
                val response = AppResponse(
                    message = "You are loggedin",
                    appCode = 200,
                    data = userJwtToken
                )
                call.respondText(AppGson.gson.toJson(response))
            }
        }
        authenticate("auth-bearer") {
            get("/users") {
                val idHeader = call.request.headers["id"]
                val userEmail: String = call.principal<UserIdPrincipal>()?.name!!
                if (idHeader == userEmail) {
                    val response = AppResponse(message = "Here are list of users", appCode = 200, data = AppStore.users)
                    call.respondText(AppGson.gson.toJson(response))
                } else {
                    val response = AppResponse<User>(
                        appCode = 400,
                        error = AppError(errorMessage = "You are not authorized, Try login again")
                    )
                    call.respond(status = HttpStatusCode.Unauthorized, message = AppGson.gson.toJson(response))
                }

            }
            get("/user/{email}") {
                val idHeader = call.request.headers["id"]
                val userEmail: String = call.principal<UserIdPrincipal>()?.name!!
                if (idHeader == userEmail) {
                    val email = call.parameters["email"] ?: ""
                    val user = AppStore.users.find { user -> user.email == email }
                    if (user != null) {
                        val response = AppResponse(message = "User Found", appCode = 200, data = user)
                        call.respondText(AppGson.gson.toJson(response))
                    } else {
                        val response =
                            AppResponse<User>(appCode = 400, error = AppError(errorMessage = "User Not Found"))
                        call.respond(status = HttpStatusCode.BadRequest, message = AppGson.gson.toJson(response))
                    }
                } else {
                    val response = AppResponse<User>(
                        appCode = 400,
                        error = AppError(errorMessage = "You are not authorized, Try login again")
                    )
                    call.respond(status = HttpStatusCode.Unauthorized, message = AppGson.gson.toJson(response))
                }
            }
        }

        post("/user") {
            val requestBody = call.receiveText().trimIndent()
            val user = AppGson.gson.fromJson(requestBody, User::class.java)
            if ((user.password.isNotBlank()) && (user.email.isNotBlank())) {
                AppStore.users.add(user)
                val response = AppResponse(message = "user has been created", appCode = 200, data = user)
                call.respond(AppGson.gson.toJson(response))
            } else if (user.password.isBlank()) {
                val response = AppResponse<User>(
                    appCode = 400,
                    error = AppError(errorMessage = "Password cannot be empty or blank")
                )
                call.respond(status = HttpStatusCode.BadRequest, message = AppGson.gson.toJson(response))
            } else {
                val response =
                    AppResponse<User>(appCode = 400, error = AppError(errorMessage = "Email cannot be empty or blank"))
                call.respond(status = HttpStatusCode.BadRequest, message = AppGson.gson.toJson(response))
            }

        }

    }
}