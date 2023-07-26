package com.azlaan95.database.daofacade.tokens

import com.azlaan95.database.DatabaseFactory.dbQuery
import com.azlaan95.models.User
import com.azlaan95.models.Users
import com.azlaan95.providers.jwt.JwtToken
import com.azlaan95.providers.jwt.JwtTokens
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class JwtTokensDaoImpl : JwtTokensDao {
    private fun resultRowToToken(row: ResultRow) = JwtToken(
        id = row[JwtTokens.id],
        accessToken = row[JwtTokens.accessToken],
        refreshToken = row[JwtTokens.refreshToken],
        userId = row[JwtTokens.userId]
    )

    private fun resultRowToUsers(row: ResultRow) = User(
        id = row[Users.id],
        name = row[Users.name],
        email = row[Users.email],
        password = row[Users.password]
    )


    override suspend fun updateToken(token: JwtToken): Boolean = dbQuery {
        JwtTokens.update({ JwtTokens.id.eq(token.id ?: 0) }) {
            it[accessToken] = token.accessToken
            it[refreshToken] = token.refreshToken
            it[userId] = token.userId
        } > 0
    }

    override suspend fun addToken(token: JwtToken): JwtToken? = dbQuery {
        val insertStatement = JwtTokens.insert {
            it[accessToken] = token.accessToken
            it[refreshToken] = token.refreshToken
            it[userId] = token.userId
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToToken)
    }

    override suspend fun getUserByToken(accessToken: String): User? {
        return dbQuery {
            (Users innerJoin JwtTokens)
                .slice(Users.columns)
                .select { JwtTokens.accessToken eq accessToken }
                .map(::resultRowToUsers)
                .singleOrNull()
        }
    }

    override suspend fun getTokens(): List<JwtToken> = dbQuery {
        JwtTokens.selectAll().map(::resultRowToToken)
    }

    override suspend fun getTokenById(id: Int): List<JwtToken>  = dbQuery {
        JwtTokens.select { JwtTokens.userId eq id }.map(::resultRowToToken)
    }


}