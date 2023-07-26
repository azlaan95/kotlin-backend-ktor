package com.azlaan95.providers.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.Date

object JWTProvider {
    private const val secret = "SECRET-HakuNaMatata"
    private const val issuer = "ISSUER-Azlaan-Khan"
    private const val accessTokenExpiryHours = 1
    private const val refreshTokenExpiryDays = 5
    private const val claimKey = "id"

    fun generateJwtToken(userId: String): JwtToken {
        val algorithm = Algorithm.HMAC256(secret)
        val issuedAt = Date()
        val accessTokenExpiresAt =
            Date(System.currentTimeMillis() + 3600 * 1000 * accessTokenExpiryHours) // Token expires in 1 hour
        val refreshTokenExpiresAt =
            Date(System.currentTimeMillis() + 3600 * 1000 * 24 * refreshTokenExpiryDays) // Token expires in 5 Days

        val accessToken = JWT.create()
            .withIssuer(issuer)
            .withSubject(userId)
            .withClaim(claimKey, userId)
            .withIssuedAt(issuedAt)
            .withExpiresAt(accessTokenExpiresAt)
            .sign(algorithm)
        val refreshToken = JWT.create()
            .withIssuer(issuer)
            .withSubject(userId)
            .withClaim(claimKey, userId)
            .withIssuedAt(issuedAt)
            .withExpiresAt(refreshTokenExpiresAt)
            .sign(algorithm)

        return JwtToken(accessToken = accessToken, refreshToken = refreshToken, userId = Integer.parseInt(userId))
    }

    fun generateToken(userId: String): String {
        val algorithm = Algorithm.HMAC256(secret)
        val issuedAt = Date()
        val accessTokenExpiresAt =
            Date(System.currentTimeMillis() + 3600 * 1000 * accessTokenExpiryHours) // Token expires in 1 hour

        return JWT.create()
            .withIssuer(issuer)
            .withSubject(userId)
            .withClaim(claimKey, userId)
            .withIssuedAt(issuedAt)
            .withExpiresAt(accessTokenExpiresAt)
            .sign(algorithm)
    }


    /*    fun authorizeUserViaToken(token: String, userId: String): Boolean{

        }*/

    fun validateTokenById(token: String, userId: String): Boolean {
        val algorithm = Algorithm.HMAC256(secret)
        return try {
            val verifier = JWT.require(algorithm).withSubject(userId).build()
            verifier.verify(token)
            true
        } catch (exception: Exception) {
            false
        }
    }

    fun isValidToken(token: String): String? {
        val algorithm = Algorithm.HMAC256(secret)
        return try {
            val verifier = JWT.require(algorithm).build()
            verifier.verify(token)
            extractUserIdFromRefreshToken(token)
        } catch (exception: Exception) {
            null
        }
    }

    fun extractUserIdFromRefreshToken(token: String): String? {
        return try {
            val decodedToken = JWT.decode(token)
            decodedToken.getClaim(claimKey).asString()
        } catch (exception: Exception) {
            null
        }
    }
}