package com.azlaan95.database.daofacade.user

import com.azlaan95.models.User

interface UsersDao {
    suspend fun getUsers(): List<User>
    suspend fun getUser(id: Int): User?
    suspend fun addUser(user: User): User?
    suspend fun updateUser(user: User): Boolean
    suspend fun deleteUser(id: Int): Boolean
}