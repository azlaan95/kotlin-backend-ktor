package com.azlaan95.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*

@Serializable
data class User(
    val id: Int = 0,
    val name: String,
    val email: String,
    val password: String,
)

object Users : Table() {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 128)
    val email = varchar("email", 1024).uniqueIndex()
    val password = varchar("password", 1024)

    override val primaryKey = PrimaryKey(id)
}