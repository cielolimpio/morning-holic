package com.morningholic.morningholiccommon.enums

import com.fasterxml.jackson.annotation.JsonValue
import com.morningholic.morningholiccommon.exception.MHException

enum class BankEnum(
    @JsonValue
    val value: String,
    val displayName: String,
) {
    KOOKMIN("KOOKMIN", "국민"),
    SHINHAN("SHINHAN", "신한"),
    KAKAO("KAKAO", "카카오뱅크"),
    WOORI("WOORI", "우리"),
    SC("SC", "SC제일"),
    INDUSTRIAL("INDUSTRIAL", "기업"),
    NONGHYUP("NONGHYUP", "농협");


    companion object {
        @JsonValue
        fun from(value: String): BankEnum {
            return BankEnum.values().firstOrNull { it.value == value }
                ?: throw MHException("Invalid BankName")
        }

        fun to(
            bankEnum: BankEnum,
        ): String {
            return bankEnum.value
        }
    }
}