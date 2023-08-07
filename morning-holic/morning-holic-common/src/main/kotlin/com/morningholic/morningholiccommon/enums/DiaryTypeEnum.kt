package com.morningholic.morningholiccommon.enums

import com.fasterxml.jackson.annotation.JsonValue
import com.morningholic.morningholiccommon.exception.MHException

enum class DiaryTypeEnum(
    @JsonValue
    val value: String,
) {
    INDOOR("INDOOR"),
    OUTDOOR("OUTDOOR");

    companion object {
        @JsonValue
        fun from(value: String): DiaryTypeEnum {
            return DiaryTypeEnum.values().firstOrNull { it.value == value }
                ?: throw MHException("Invalid Diary Type")
        }

        fun to(
            diaryTypeEnum: DiaryTypeEnum,
        ): String {
            return diaryTypeEnum.value
        }
    }
}