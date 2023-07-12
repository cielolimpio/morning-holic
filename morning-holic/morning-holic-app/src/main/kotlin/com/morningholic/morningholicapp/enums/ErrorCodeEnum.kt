package com.morningholic.morningholicapp.enums

enum class ErrorCodeEnum(
    val code: Int
) {
    // AUTH_SERVICE
    SIGNUP_FORM_BLANK(1001),
    ALREADY_EXISTED_PHONE_NUMBER(1002),
    ALREADY_EXISTED_NICKNAME(1003),
    USER_NOT_FOUND(1004),
    WRONG_PASSWORD(1005),
}