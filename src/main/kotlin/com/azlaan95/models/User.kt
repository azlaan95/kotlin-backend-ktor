package com.azlaan95.models

import kotlinx.serialization.Serializable

@Serializable
data class User(val name: String, val email: String, val password: String)

