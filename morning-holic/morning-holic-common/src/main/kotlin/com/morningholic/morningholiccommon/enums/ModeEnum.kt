package com.morningholic.morningholiccommon.enums

import com.fasterxml.jackson.annotation.JsonValue
import com.morningholic.morningholiccommon.exception.MHException

enum class ModeEnum(
    @JsonValue
    val value: String
) {
    MILD("MILD"),
    CHALLENGE("CHALLENGE");

    companion object {
        @JsonValue
        fun from(value: String): ModeEnum {
            return ModeEnum.values().firstOrNull { it.value == value }
                ?: throw MHException("Invalid Mode")
        }

        fun to(
            modeEnum: ModeEnum,
        ): String {
            return modeEnum.value
        }
    }
}