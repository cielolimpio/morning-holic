package com.morningholic.morningholicapp.services

import com.morningholic.morningholicapp.enums.ErrorCodeEnum
import com.morningholic.morningholicapp.securities.JwtToken
import com.morningholic.morningholicapp.securities.JwtUtils
import com.morningholic.morningholiccommon.entities.Users
import com.morningholic.morningholiccommon.enums.RoleEnum
import com.morningholic.morningholiccommon.enums.UserStatusEnum
import com.morningholic.morningholiccommon.exception.MHException
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
    fun signUp(
        name: String,
        phoneNumber: String,
        password: String,
        nickname: String,
    ): JwtToken {
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

        return transaction {
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

            val userId = Users.insertAndGetId {
                it[this.name] = name
                it[this.phoneNumber] = phoneNumber
                it[this.password] = passwordEncoder.encode(password)
                it[this.role] = RoleEnum.USER
                it[this.nickname] = nickname
                it[this.status] = UserStatusEnum.INITIAL
                it[this.createdAt] = LocalDateTime.now()
                it[this.updatedAt] = LocalDateTime.now()
            }.value

            JwtUtils.createToken(userId)
        }
    }
}