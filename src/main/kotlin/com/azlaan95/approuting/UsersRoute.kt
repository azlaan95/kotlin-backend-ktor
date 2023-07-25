package com.azlaan95.approuting

import com.azlaan95.database.AppStore
import com.azlaan95.models.AppError
import com.azlaan95.models.AppResponse
import com.azlaan95.models.User
import com.azlaan95.util.AppGson
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.usersRoute() {
    route("/users") {

        get("/all") {
            val idHeader = call.request.headers["id"]
            val userEmail: String = call.principal<UserIdPrincipal>()?.name!!
            if (idHeader == userEmail) {
                val response = AppResponse(message = "Here are list of users", appCode = 200, data = AppStore.users)
                call.respond(AppGson.gson.toJson(response))
            } else {
                val response = AppResponse<User>(
                    appCode = 400,
                    error = AppError(errorMessage = "You are not authorized, Try login again")
                )
                call.respond(status = HttpStatusCode.Unauthorized, message = AppGson.gson.toJson(response))
            }

        }
        get("/{email}") {
            val idHeader = call.request.headers["id"]
            val userEmail: String = call.principal<UserIdPrincipal>()?.name!!
            if (idHeader == userEmail) {
                val email = call.parameters["email"] ?: ""
                val user = AppStore.users.find { user -> user.email == email }
                if (user != null) {
                    val response = AppResponse(message = "User Found", appCode = 200, data = user)
                    call.respond(AppGson.gson.toJson(response))
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
        get {
            val idHeader = call.request.headers["id"]
            val userEmail: String = call.principal<UserIdPrincipal>()?.name!!
            if (idHeader == userEmail) {
                val parameters = call.request.queryParameters
                val email = parameters["email"]
                val user = AppStore.users.find { user -> user.email == email }
                if (user != null) {
                    val response = AppResponse(message = "User Found", appCode = 200, data = user)
                    call.respond(AppGson.gson.toJson(response))
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
}