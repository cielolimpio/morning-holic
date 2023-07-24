package com.morningholic.morningholicapp.payloads.response

import com.morningholic.morningholiccommon.enums.ModeEnum
import java.time.LocalDateTime

data class RegisterResponse(
    val targetWakeUpTime: LocalDateTime,
    val refundBankNameAndAccount: String,
    val mode: ModeEnum,
)
