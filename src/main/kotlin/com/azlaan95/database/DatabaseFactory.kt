package com.azlaan95.database

import com.azlaan95.models.Users
import com.azlaan95.providers.jwt.JwtTokens
import io.ktor.server.config.*
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {

    /* val host = "sql6.freesqldatabase.com"
     val databaseName = "sql6635377"
     val databaseUser = "sql6635377"
     val databasePassword = "s5Ie7VngNp"
     val databaseDriver = "com.mysql.jdbc.Driver"
     val port = 3306
     val url = "jdbc:mysql://$host:$port/$databaseName"*/
    fun init(config: ApplicationConfig) {
        // Access specific values from the configuration
        val h2Url = config.property("ktor.database.h2.url").getString()
        val h2Driver = config.property("ktor.database.h2.driver").getString()
        println("//////////////////// DATABASE INITILIZING ////////////////////////")
        println("//////////////////// CONFIG IS ////////////////////////")
        println("//////////////////// H2_URL ${h2Url} ////////////////////////")
        println("//////////////////// H2_DRIVER ${h2Driver} ////////////////////////")
        println("//////////////////// DATABASE INITILIZING ////////////////////////")

        /*val driverClassName = "org.h2.Driver"
        val jdbcURL = "jdbc:h2:file:./build/db"*/
        //val database = Database.connect(url, databaseDriver, databaseUser, databasePassword)
        val database = Database.connect(h2Url, h2Driver)
        transaction(database) {
            SchemaUtils.create(Users, JwtTokens)
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}
