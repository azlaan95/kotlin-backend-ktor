package com.azlaan95.providers.jwt

data class JwtToken(val accessToken: String, val refreshToken: String, val id: String)
