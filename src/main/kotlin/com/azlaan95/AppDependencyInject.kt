package com.azlaan95

import com.azlaan95.database.daofacade.tokens.JwtTokensDao
import com.azlaan95.database.daofacade.tokens.JwtTokensDaoImpl
import com.azlaan95.database.daofacade.user.UsersDao
import com.azlaan95.database.daofacade.user.UsersDaoImpl

object AppDependencyInject {
    fun userDaoProvider(): UsersDao = UsersDaoImpl()

    fun tokenDaoProvider(): JwtTokensDao = JwtTokensDaoImpl()
}