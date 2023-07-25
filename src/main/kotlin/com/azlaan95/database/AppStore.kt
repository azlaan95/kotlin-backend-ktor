package com.azlaan95.database

import com.azlaan95.models.User
import com.azlaan95.providers.jwt.JwtToken


object AppStore {
    var users = ArrayList<User>()
    var tokens = mutableMapOf<String, JwtToken>()
    fun addToken(id: String, token: JwtToken) {
        tokens[id] = token
    }

    fun compareToken(id: String, token: String): Boolean {
        val savesToken = tokens[id]
        return token == savesToken?.accessToken
    }
}