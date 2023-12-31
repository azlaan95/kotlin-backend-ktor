package com.azlaan95.approuting

import com.azlaan95.database.daofacade.user.UsersDao
import com.azlaan95.models.AppError
import com.azlaan95.models.AppResponse
import com.azlaan95.models.User
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.runBlocking

fun Route.usersRoute(usersDao: UsersDao) {
    route("/users") {
        get("/all") {
            val idHeader = call.request.headers["id"]
            val userEmail: String = call.principal<UserIdPrincipal>()?.name!!
            if (idHeader == userEmail) {
                runBlocking {
                    var users: List<User> = usersDao.getUsers()
                    val response = AppResponse(message = "Here are list of users", appCode = 200, data = users)
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
        get("/{email}") {
            val idHeader = call.request.headers["id"]
            val userEmail: String = call.principal<UserIdPrincipal>()?.name!!
            if (idHeader == userEmail) {
                val email = call.parameters["email"] ?: ""
                runBlocking {
                    var user: User? = usersDao.getUserByEmail(email)
                    if (user != null) {
                        val response = AppResponse(message = "User Found", appCode = 200, data = user)
                        call.respond(response)
                    } else {
                        val response =
                            AppResponse<User>(appCode = 400, error = AppError(errorMessage = "User Not Found"))
                        call.respond(status = HttpStatusCode.BadRequest, message = response)
                    }
                }
            } else {
                val response = AppResponse<User>(
                    appCode = 400,
                    error = AppError(errorMessage = "You are not authorized, Try login again")
                )
                call.respond(status = HttpStatusCode.Unauthorized, message = response)
            }
        }
        get {
            val idHeader = call.request.headers["id"]
            val userEmail: String = call.principal<UserIdPrincipal>()?.name!!
            if (idHeader == userEmail) {
                val parameters = call.request.queryParameters
                val email = parameters["email"]
                if (email != null) {
                    runBlocking {
                        var user: User? = usersDao.getUserByEmail(email)
                        if (user != null) {
                            val response = AppResponse(message = "User Found", appCode = 200, data = user)
                            call.respond(response)
                        } else {
                            val response =
                                AppResponse<User>(appCode = 400, error = AppError(errorMessage = "User Not Found"))
                            call.respond(status = HttpStatusCode.BadRequest, message = response)
                        }
                    }
                } else {
                    val response =
                        AppResponse<User>(appCode = 400, error = AppError(errorMessage = "Please provide Email"))
                    call.respond(status = HttpStatusCode.BadRequest, message = response)

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