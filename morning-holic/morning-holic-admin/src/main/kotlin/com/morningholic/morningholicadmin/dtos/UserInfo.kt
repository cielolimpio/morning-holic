package com.morningholic.morningholicadmin.dtos

import com.morningholic.morningholiccommon.enums.ModeEnum
import java.time.LocalDateTime

data class UserInfo(
    val name: String,
    val phoneNumber: String,
    val nickname: String,
    val targetWakeUpTime: LocalDateTime?,
    val refundBankName: String?,
    val refundAccount: String?,
    val mode: ModeEnum?,
    val createdAt: LocalDateTime,
)