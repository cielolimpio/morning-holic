package com.morningholic.morningholicapp.payloads.response

import com.morningholic.morningholicapp.dtos.TargetWakeUpTimeDto
import com.morningholic.morningholiccommon.enums.ModeEnum

data class RegisterResponse(
    val targetWakeUpTime: TargetWakeUpTimeDto,
    val refundBankNameAndAccount: String,
    val mode: ModeEnum,
)
