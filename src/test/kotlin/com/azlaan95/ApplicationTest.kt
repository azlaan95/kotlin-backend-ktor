package com.azlaan95

import com.azlaan95.providers.jwt.JwtToken
import com.azlaan95.util.AppGson
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {

    @Test
    fun initialize() = testApplication {
        application {
        }
    }

    @Test
    fun testRegister() = testApplication {
        client.post("/register") {
            setBody(
                """
                {
                    "name": "Azlaan Khan",
                    "email":"azlaan@gmail.com",
                    "password":"123456"
                }
            """
            )
        }.apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals(
                """{"message":"user has been created","appCode":200,"data":{"name":"Azlaan Khan","email":"azlaan@gmail.com","password":"123456"}}""",
                bodyAsText()
            )
        }

        var myToken: JwtToken
        client.get("/authenticate/login") {
            basicAuth("azlaan@gmail.com", "123456")
        }.apply {
            myToken = AppGson.gson.fromJson(bodyAsText(), JwtToken::class.java)
            assertEquals(HttpStatusCode.OK, status)
        }

        client.get("/users/all") {
            bearerAuth(myToken.accessToken)
            header("id", myToken.id)
        }.apply {
            assertEquals(HttpStatusCode.OK, status)
        }
    }

}
