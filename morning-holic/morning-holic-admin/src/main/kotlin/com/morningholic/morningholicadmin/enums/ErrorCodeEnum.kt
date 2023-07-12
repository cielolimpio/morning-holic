package com.morningholic.morningholicadmin.enums

enum class ErrorCodeEnum(
    val code: Int
) {
    // AUTH_SERVICE
    USER_NOT_FOUND(1001),
    WRONG_PASSWORD(1002),
    NOT_ADMIN_USER(1003),
}