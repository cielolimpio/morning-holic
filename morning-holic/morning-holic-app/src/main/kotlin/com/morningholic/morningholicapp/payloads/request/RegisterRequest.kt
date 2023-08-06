package com.morningholic.morningholicapp.payloads.request

import com.morningholic.morningholicapp.dtos.TargetWakeUpTimeDto
import com.morningholic.morningholiccommon.enums.BankEnum
import com.morningholic.morningholiccommon.enums.ModeEnum

data class RegisterRequest(
    val targetWakeUpTime: TargetWakeUpTimeDto,
    val refundBankName: BankEnum,
    val refundAccount: String,
    val mode: ModeEnum,
)