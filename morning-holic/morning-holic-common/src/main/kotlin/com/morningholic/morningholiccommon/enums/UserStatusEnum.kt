package com.morningholic.morningholiccommon.enums

import com.fasterxml.jackson.annotation.JsonValue
import com.morningholic.morningholiccommon.exception.MHException

enum class UserStatusEnum(
    @JsonValue
    val value: String
) {
    INITIAL("INITIAL"),
    REQUEST("REQUEST"),
    ACCEPT("ACCEPT"),
    REJECT("REJECT");

    companion object {
        @JsonValue
        fun from(value: String): UserStatusEnum {
            return UserStatusEnum.values().firstOrNull { it.value == value }
                ?: throw MHException("Invalid User Status")
        }

        fun to(
            userStatusEnum: UserStatusEnum,
        ): String {
            return userStatusEnum.value
        }
    }
}