package com.azlaan95.database.daofacade.tokens

import com.azlaan95.models.User
import com.azlaan95.providers.jwt.JwtToken

interface JwtTokensDao {
    suspend fun updateToken(token: JwtToken): Boolean
    suspend fun addToken(token: JwtToken): JwtToken?
    suspend fun getUserByToken(accessToken: String): User?
    suspend fun getTokens(): List<JwtToken>
    suspend fun getTokenById(id: Int): List<JwtToken>
}