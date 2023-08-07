package com.morningholic.morningholiccommon.enums

import com.fasterxml.jackson.annotation.JsonValue
import com.morningholic.morningholiccommon.exception.MHException

enum class DiaryImageTypeEnum(
    @JsonValue
    val value: String,
) {
    WAKE_UP("WAKE_UP"),
    ROUTINE_START("ROUTINE_START"),
    ROUTINE_END("ROUTINE_END");

    companion object {
        @JsonValue
        fun from(value: String): DiaryImageTypeEnum {
            return DiaryImageTypeEnum.values().firstOrNull { it.value == value }
                ?: throw MHException("Invalid Diary Image Type")
        }

        fun to(
            diaryImageTypeEnum: DiaryImageTypeEnum,
        ): String {
            return diaryImageTypeEnum.value
        }
    }
}