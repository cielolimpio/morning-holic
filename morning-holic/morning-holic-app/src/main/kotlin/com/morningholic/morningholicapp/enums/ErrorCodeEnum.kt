package com.morningholic.morningholicapp.enums

enum class ErrorCodeEnum(
    val code: Int
) {
    // AUTH_SERVICE    1000
    SIGNUP_FORM_BLANK(1001),
    ALREADY_EXISTED_PHONE_NUMBER(1002),
    ALREADY_EXISTED_NICKNAME(1003),
    USER_NOT_FOUND(1004),
    WRONG_PASSWORD(1005),

    // USER_SERVICE    2000
    BANK_NAME_BLANK(2001),
    BANK_ACCOUNT_BLANK(2002),
    MODE_BLANK(2003),
}