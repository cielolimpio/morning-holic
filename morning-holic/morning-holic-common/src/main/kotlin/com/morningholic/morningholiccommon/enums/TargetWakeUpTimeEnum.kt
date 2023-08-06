package com.morningholic.morningholiccommon.enums

import com.fasterxml.jackson.annotation.JsonValue
import com.morningholic.morningholiccommon.exception.MHException

enum class TargetWakeUpTimeEnum(
    @JsonValue
    val value: String,
    val hour: Int,
    val minute: Int,
) {
    FOUR("FOUR", 4, 0),
    FOUR_THIRTY("FOUR_THIRTY", 4, 30),
    FIVE("FIVE", 5, 0),
    FIVE_THIRTY("FIVE_THIRTY", 5, 30),
    SIX("SIX", 6, 0),
    SIX_THIRTY("SIX_THIRTY", 6, 30),
    SEVEN("SEVEN", 7, 0),
    SEVEN_THIRTY("SEVEN_THIRTY", 7, 30),
    EIGHT("EIGHT", 8, 0),
    ;

    companion object {
        @JsonValue
        fun from(value: String): TargetWakeUpTimeEnum {
            return TargetWakeUpTimeEnum.values().firstOrNull { it.value == value }
                ?: throw MHException("Invalid Target Wake Up Time")
        }

        fun to(
            targetWakeUpTimeEnum: TargetWakeUpTimeEnum,
        ): String {
            return targetWakeUpTimeEnum.value
        }

        fun toEnum(
            hour: Int,
            minute: Int,
        ): TargetWakeUpTimeEnum {
            return TargetWakeUpTimeEnum.values().firstOrNull { it.hour == hour && it.minute == minute }
                ?: throw MHException("Invalid Target Wake Up Time")
        }
    }
}