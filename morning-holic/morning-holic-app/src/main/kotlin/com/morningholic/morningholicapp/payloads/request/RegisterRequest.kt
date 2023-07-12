package com.morningholic.morningholicapp.payloads.request

import com.morningholic.morningholiccommon.enums.BankEnum
import com.morningholic.morningholiccommon.enums.ModeEnum
import java.time.LocalDateTime

data class RegisterRequest(
    val targetWakeUpTime: LocalDateTime,
    val refundBankName: BankEnum,
    val refundAccount: String,
    val mode: ModeEnum,
)