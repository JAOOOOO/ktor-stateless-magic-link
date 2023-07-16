package com.jaooooo.services

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.*


class TokenService(
    private val secret: String,
    private val issuer: String,
    private val audience: String,
    private val realm: String
) {


    fun generateToken(claims: Map<String, String>): String {
        return JWT.create()
            .withAudience(audience)
            .withIssuer(issuer)
            .apply {
                claims.forEach { (key, value) -> withClaim(key, value) }
            }
            .withExpiresAt(Date(System.currentTimeMillis() + 60000))
            .sign(Algorithm.HMAC256(secret))
    }


    fun validateToken(token: String, claims: Map<String, String>): Boolean {
        try {
            val verifier = JWT.require(Algorithm.HMAC256(secret))
                .withAudience(audience)
                .withIssuer(issuer)
                .apply {
                    claims.forEach { (key, value) -> withClaim(key, value) }
                }
                .build()
            verifier.verify(token)
            return true
        } catch (e: Exception) {
            println(e.message)
        }
        return false
    }

}