package com.azlaan95.database.daofacade.user

import com.azlaan95.database.DatabaseFactory.dbQuery
import com.azlaan95.models.User
import com.azlaan95.models.Users
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class UsersDaoImpl : UsersDao {

    private fun resultRowToUsers(row: ResultRow) = User(
        id = row[Users.id],
        name = row[Users.name],
        email = row[Users.email],
        password = row[Users.password]
    )

    override suspend fun getUsers(): List<User> = dbQuery {
        Users.selectAll().map(::resultRowToUsers)
    }

    override suspend fun getUser(id: Int): User? = dbQuery {
        Users
            .select { Users.id eq id }
            .map(::resultRowToUsers)
            .singleOrNull()
    }

    override suspend fun addUser(user: User): User? = dbQuery {
        val insertStatement = Users.insert {
            it[email] = user.email
            it[name] = user.name
            it[password] = user.password
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToUsers)
    }

    override suspend fun updateUser(user: User): Boolean = dbQuery {
        Users.update({ Users.id eq user.id }) {
            it[name] = user.name
            it[email] = user.email
            it[password] = user.password
        } > 0
    }

    override suspend fun deleteUser(id: Int): Boolean = dbQuery {
        Users.deleteWhere { Users.id eq id } > 0
    }
}