package com.morningholic.morningholicapp.dtos

import com.morningholic.morningholiccommon.enums.BankEnum
import com.morningholic.morningholiccommon.enums.ModeEnum
import com.morningholic.morningholiccommon.enums.UserStatusEnum

data class UserInfo(
    val name: String,
    val phoneNumber: String,
    val nickname: String,
    val targetWakeUpTime: TargetWakeUpTimeDto?,
    val refundBankName: BankEnum?,
    val refundAccount: String?,
    val mode: ModeEnum?,
    val status: UserStatusEnum,
)
