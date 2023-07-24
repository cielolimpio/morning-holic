package com.morningholic.morningholiccommon.enums

import com.fasterxml.jackson.annotation.JsonValue
import com.morningholic.morningholiccommon.exception.MHException

enum class UserRegisterStatusEnum(
    @JsonValue
    val value: String
) {
    REQUEST("REQUEST"),
    ACCEPT("ACCEPT"),
    REJECT("REJECT");

    companion object {
        @JsonValue
        fun from(value: String): UserRegisterStatusEnum {
            return UserRegisterStatusEnum.values().firstOrNull { it.value == value }
                ?: throw MHException("Invalid User Register Status")
        }

        fun to(
            userRegisterStatusEnum: UserRegisterStatusEnum,
        ): String {
            return userRegisterStatusEnum.value
        }
    }
}