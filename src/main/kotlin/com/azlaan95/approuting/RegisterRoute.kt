package com.azlaan95.approuting

import com.azlaan95.database.daofacade.user.UsersDao
import com.azlaan95.models.AppError
import com.azlaan95.models.AppResponse
import com.azlaan95.models.User
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.runBlocking

fun Route.registerRoute(userDao: UsersDao) {
    route("/register") {
        post {
            val user: User = call.receive<User>()
            if ((user.password.isNotBlank()) && (user.email.isNotBlank())) {
                runBlocking {
                    var registeredUser: User? = userDao.addUser(user)
                    if (registeredUser != null) {
                        val response =
                            AppResponse(message = "user has been created", appCode = 200, data = registeredUser)
                        call.respond(response)
                    } else {
                        val response =
                            AppResponse<User>(appCode = 400, error = AppError(errorMessage = "Something went wrong"))
                        call.respond(status = HttpStatusCode.BadRequest, message = response)
                    }

                }
            } else if (user.password.isBlank()) {
                val response = AppResponse<User>(
                    appCode = 400,
                    error = AppError(errorMessage = "Password cannot be empty or blank")
                )
                call.respond(status = HttpStatusCode.BadRequest, message = response)
            } else {
                val response =
                    AppResponse<User>(appCode = 400, error = AppError(errorMessage = "Email cannot be empty or blank"))
                call.respond(status = HttpStatusCode.BadRequest, message = response)
            }

        }
    }
}