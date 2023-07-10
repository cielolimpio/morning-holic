package com.morningholic.morningholiccommon.enums

import com.fasterxml.jackson.annotation.JsonValue
import com.morningholic.morningholiccommon.exception.MHException

enum class RoleEnum(
    @JsonValue
    val value: String
) {
    ADMIN("ADMIN"),
    USER("USER");

    companion object {
        @JsonValue
        fun from(value: String): RoleEnum {
            return RoleEnum.values().firstOrNull { it.value == value }
                ?: throw MHException("Invalid Role")
        }

        fun to(
            roleEnum: RoleEnum,
        ): String {
            return roleEnum.value
        }
    }
}