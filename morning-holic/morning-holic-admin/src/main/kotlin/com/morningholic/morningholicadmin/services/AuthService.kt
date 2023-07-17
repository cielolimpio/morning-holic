package com.morningholic.morningholicadmin.services

import com.morningholic.morningholicadmin.enums.ErrorCodeEnum
import com.morningholic.morningholicadmin.securities.JwtToken
import com.morningholic.morningholicadmin.securities.JwtUtils
import com.morningholic.morningholiccommon.entities.Users
import com.morningholic.morningholiccommon.enums.RoleEnum
import com.morningholic.morningholiccommon.exception.MHException
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val passwordEncoder: PasswordEncoder
) {
    fun login(
        phoneNumber: String,
        password: String,
    ): JwtToken {
        return transaction {
            Users.select { Users.phoneNumber eq phoneNumber and Users.deletedAt.isNull() }.singleOrNull()
                ?.let {
                    if (!passwordEncoder.matches(password, it[Users.password]))
                        throw MHException(
                            ErrorCodeEnum.WRONG_PASSWORD.code,
                            HttpStatus.BAD_REQUEST,
                            "Wrong Password"
                        )

                    if (it[Users.role] != RoleEnum.ADMIN)
                        throw MHException(
                            ErrorCodeEnum.NOT_ADMIN_USER.code,
                            HttpStatus.BAD_REQUEST,
                            "Not Admin User"
                        )

                    JwtUtils.createToken(it[Users.id].value)
                }
                ?: throw MHException(
                    ErrorCodeEnum.USER_NOT_FOUND.code,
                    HttpStatus.NOT_FOUND,
                    "User Not Found"
                )
        }
    }

    fun refreshToken(refreshToken: String): JwtToken {
        if (JwtUtils.validateRefreshToken(refreshToken)) {
            val userId = JwtUtils.parseUserIdFromToken(refreshToken)
            return JwtUtils.createToken(userId)
        } else throw MHException(
            ErrorCodeEnum.REFRESH_TOKEN_EXPIRED.code,
            HttpStatus.UNAUTHORIZED,
            "Refresh Token Expired"
        )
    }
}