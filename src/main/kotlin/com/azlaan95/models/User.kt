package com.azlaan95.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*

@Serializable
data class User(
    val id: Int,
    val name: String,
    val email: String,
    val password: String,
    val token: String?,
    val refreshToken: String?
)

object Users : Table() {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 128)
    val email = varchar("email", 1024)
    val password = varchar("password", 1024)
    val token = largeText("token").nullable()
    val refreshToken = largeText("refreshToken").nullable()

    override val primaryKey = PrimaryKey(id)
}