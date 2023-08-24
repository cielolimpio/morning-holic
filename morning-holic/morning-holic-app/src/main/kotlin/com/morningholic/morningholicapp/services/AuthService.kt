package com.morningholic.morningholicapp.services

import com.morningholic.morningholicapp.enums.ErrorCodeEnum
import com.morningholic.morningholicapp.securities.JwtToken
import com.morningholic.morningholicapp.securities.JwtUtils
import com.morningholic.morningholiccommon.entities.Users
import com.morningholic.morningholiccommon.enums.RoleEnum
import com.morningholic.morningholiccommon.enums.UserStatusEnum
import com.morningholic.morningholiccommon.exception.MHException
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class AuthService(
    private val passwordEncoder: PasswordEncoder
) {
    fun validateSignUp(phoneNumber: String): Boolean {
        return transaction {
            Users.select { Users.phoneNumber eq phoneNumber }.empty()
        }
    }

    fun signUp(
        name: String,
        phoneNumber: String,
        password: String,
        profileEmoji: String,
        nickname: String,
    ): JwtToken {
        return transaction {
            validateSignUp(name, phoneNumber, password, nickname)

            val userId = Users.insertAndGetId {
                it[this.name] = name
                it[this.phoneNumber] = phoneNumber
                it[this.password] = passwordEncoder.encode(password)
                it[this.role] = RoleEnum.USER
                it[this.profileEmoji] = profileEmoji
                it[this.nickname] = nickname
                it[this.status] = UserStatusEnum.INITIAL
                it[this.createdAt] = LocalDateTime.now()
                it[this.updatedAt] = LocalDateTime.now()
            }.value

            JwtUtils.createToken(userId)
        }
    }

    private fun validateSignUp(
        name: String,
        phoneNumber: String,
        password: String,
        nickname: String,
    ) {
        if (name.isBlank()) {
            throw MHException(
                ErrorCodeEnum.SIGNUP_FORM_BLANK.code,
                HttpStatus.BAD_REQUEST,
                "Name is blank"
            )
        }
        if (phoneNumber.isBlank()) {
            throw MHException(
                ErrorCodeEnum.SIGNUP_FORM_BLANK.code,
                HttpStatus.BAD_REQUEST,
                "Phone Number is blank"
            )
        }
        if (password.isBlank()) {
            throw MHException(
                ErrorCodeEnum.SIGNUP_FORM_BLANK.code,
                HttpStatus.BAD_REQUEST,
                "Password is blank"
            )
        }

        if (!Users.select { Users.phoneNumber eq phoneNumber }.empty())
            throw MHException(
                ErrorCodeEnum.ALREADY_EXISTED_PHONE_NUMBER.code,
                HttpStatus.CONFLICT,
                "This Phone Number Already Exists"
            )

        if (!Users.select { Users.nickname eq nickname }.empty())
            throw MHException(
                ErrorCodeEnum.ALREADY_EXISTED_NICKNAME.code,
                HttpStatus.CONFLICT,
                "This Nickname Already Exists"
            )
    }

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