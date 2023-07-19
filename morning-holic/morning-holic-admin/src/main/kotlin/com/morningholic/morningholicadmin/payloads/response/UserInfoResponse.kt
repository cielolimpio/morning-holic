package com.morningholic.morningholicadmin.payloads.response

import com.morningholic.morningholicadmin.dtos.UserInfo
import com.morningholic.morningholiccommon.enums.ModeEnum
import java.time.LocalDateTime

data class UserInfoResponse(
    val userId: Long,
    val name: String,
    val phoneNumber: String,
    val nickname: String,
    val targetWakeUpTime: LocalDateTime?,
    val refundBankNameAndAccount: String?,
    val mode: ModeEnum?,
    val createdAt: LocalDateTime,
) {
    companion object {
        fun UserInfo.toResponse(): UserInfoResponse {
            return UserInfoResponse(
                userId = this.userId,
                name = this.name,
                phoneNumber = this.phoneNumber,
                nickname = this.nickname,
                targetWakeUpTime = this.targetWakeUpTime,
                refundBankNameAndAccount = this.refundAccount?.let { "${this.refundBankName} ${this.refundAccount}" },
                mode = this.mode,
                createdAt = this.createdAt,
            )
        }
    }
}
