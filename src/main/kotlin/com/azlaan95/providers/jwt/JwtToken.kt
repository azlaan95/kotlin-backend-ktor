package com.azlaan95.providers.jwt

import com.azlaan95.models.Users
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

@Serializable
data class JwtToken(val accessToken: String, val refreshToken: String, val userId: Int, val id: Int?=null)


object JwtTokens : Table() {
    val id = integer("id").autoIncrement()
    val accessToken = largeText("accessToken")
    val refreshToken = largeText("refreshToken")
    val userId = integer("userId").references(Users.id)

    override val primaryKey = PrimaryKey(id)
}