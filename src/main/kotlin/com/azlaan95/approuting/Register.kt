package com.azlaan95.approuting

import com.azlaan95.database.AppStore
import com.azlaan95.database.daofacade.user.UsersDao
import com.azlaan95.database.daofacade.user.UsersDaoImpl
import com.azlaan95.models.AppError
import com.azlaan95.models.AppResponse
import com.azlaan95.models.User
import com.azlaan95.util.AppGson
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.runBlocking

fun Route.registerRoute() {
    route("/register") {
        post {
            val requestBody = call.receiveText().trimIndent()
            val user = AppGson.gson.fromJson(requestBody, User::class.java)
            if ((user.password.isNotBlank()) && (user.email.isNotBlank())) {
                //AppStore.users.add(user)
                val dao: UsersDao = UsersDaoImpl();
                runBlocking {
                    dao.addUser(user)
                    val response = AppResponse(message = "user has been created", appCode = 200, data = user)
                    call.respond(AppGson.gson.toJson(response))
                }
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