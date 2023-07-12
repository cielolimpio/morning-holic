package com.morningholic.morningholicadmin.securities

import com.morningholic.morningholiccommon.entities.Users
import com.morningholic.morningholiccommon.exception.MHException
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import java.util.*

object JwtUtils {
    private const val MINUTE = 60 * 1000L
    private const val HOUR = 60 * MINUTE
    private const val DAY = 24 * HOUR

    private val JWT_SECRET = "SECRETSECRETSECRETSECRETSECRETSECRET".toByteArray()
    private val SIGNATURE_ALG = SignatureAlgorithm.HS256

    fun createToken(userId: Long): JwtToken {
        val claims = Jwts.claims()
        claims["userId"] = userId
        val now = Date()

        val accessToken = Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(Date(now.time + (10 * DAY)))
            .signWith(Keys.hmacShaKeyFor(JWT_SECRET), SIGNATURE_ALG)
            .compact()

        val refreshToken = Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(Date(now.time + (60 * DAY)))
            .signWith(Keys.hmacShaKeyFor(JWT_SECRET), SIGNATURE_ALG)
            .compact()

        return JwtToken (
            userId = userId,
            accessToken = accessToken,
            refreshToken = refreshToken,
        )
    }

    fun validateAccessToken(accessToken: String): Boolean {
        try {
            val claims = getAllClaims(accessToken)
            return claims.expiration.after(Date())
        } catch (e: Exception) {
            throw MHException(HttpStatus.FORBIDDEN, "Invalid Access Token")
        }
    }

    fun validateRefreshToken(refreshToken: String): Boolean {
        try {
            val claims = getAllClaims(refreshToken)
            return claims.expiration.after(Date())
        } catch (e: Exception) {
            throw MHException(HttpStatus.FORBIDDEN, "Invalid Refresh Token")
        }
    }

    fun parseUserIdFromToken(token: String): Long {
        val claims = getAllClaims(token)
        return claims["userId"] as Long
    }

    fun getAuthenticationByUserId(userId: Long): Authentication {
        val userDetails = transaction {
            Users.select { Users.id eq userId and Users.deletedAt.isNull() }.firstOrNull()
                ?.let {
                    UserDetailsImpl(
                        userId = it[Users.id].value,
                        phoneNumber = it[Users.phoneNumber],
                        password = it[Users.password],
                        role = it[Users.role],
                    )
                } ?: throw MHException(HttpStatus.NOT_FOUND, "존재하지 않는 유저입니다.")
        }
        return UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
    }

    private fun getAllClaims(token: String): Claims {
        try {
            return Jwts.parserBuilder()
                .setSigningKey(JWT_SECRET)
                .build()
                .parseClaimsJws(token)
                .body
        } catch (e: Exception) {
            throw Exception()
        }
    }
}