package com.morningholic.morningholiccommon.enums

import com.fasterxml.jackson.annotation.JsonValue
import com.morningholic.morningholiccommon.exception.MHException

enum class DiaryStatusEnum(
    @JsonValue
    val value: String
) {
    INITIAL("INITIAL"),
    DONE("DONE");

    companion object {
        @JsonValue
        fun from(value: String): DiaryStatusEnum {
            return DiaryStatusEnum.values().firstOrNull { it.value == value }
                ?: throw MHException("Invalid Diary Status")
        }

        fun to(
            diaryStatusEnum: DiaryStatusEnum,
        ): String {
            return diaryStatusEnum.value
        }
    }
}