package com.morningholic.morningholicadmin.enums

enum class ErrorCodeEnum(
    val code: Int
) {
    // JWT_TOKEN
    INVALID_ACCESS_TOKEN(1),
    INVALID_REFRESH_TOKEN(2),
    ACCESS_TOKEN_EXPIRED(3),
    REFRESH_TOKEN_EXPIRED(4),

    // AUTH_SERVICE
    USER_NOT_FOUND(1001),
    WRONG_PASSWORD(1002),
    NOT_ADMIN_USER(1003),
}