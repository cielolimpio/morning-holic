package com.morningholic.morningholicapp.services

import com.morningholic.morningholicapp.dtos.TargetWakeUpTimeDto
import com.morningholic.morningholicapp.dtos.UserInfo
import com.morningholic.morningholicapp.enums.ErrorCodeEnum
import com.morningholic.morningholiccommon.entities.UserRegisterHistories
import com.morningholic.morningholiccommon.entities.Users
import com.morningholic.morningholiccommon.enums.*
import com.morningholic.morningholiccommon.exception.MHException
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class UserService {
    fun getUserInfo(userId: Long): UserInfo {
        return transaction {
            Users.select { Users.id eq userId }.firstOrNull()
                ?.let {
                    val rejectReason = if (it[Users.status] == UserStatusEnum.REJECT) {
                        UserRegisterHistories
                            .select { UserRegisterHistories.user eq userId }
                            .last()[UserRegisterHistories.rejectReason]
                    } else null

                    UserInfo(
                        name = it[Users.name],
                        phoneNumber = it[Users.phoneNumber],
                        profileEmoji = it[Users.profileEmoji],
                        nickname = it[Users.nickname],
                        targetWakeUpTime = it[Users.targetWakeUpTime]?.let { targetWakeUpTime ->
                            TargetWakeUpTimeDto(
                                hour = targetWakeUpTime.hour,
                                minute = targetWakeUpTime.minute,
                            )
                        },
                        refundBankName = it[Users.refundBankName],
                        refundAccount = it[Users.refundAccount],
                        mode = it[Users.mode],
                        status = it[Users.status],
                        rejectReason = rejectReason,
                    )
                }
                ?: throw MHException("User Not Found")
        }
    }

    fun getUserStatusAndRejectReason(userId: Long): Pair<UserStatusEnum, String?> {
        return transaction {
            val userStatus = Users.select { Users.id eq userId }.firstOrNull()
                ?.let { it[Users.status] }
                ?: throw MHException("User Not Found")

            if (userStatus == UserStatusEnum.REJECT) {
                val rejectReason = UserRegisterHistories
                    .select { UserRegisterHistories.user eq userId }
                    .last()[UserRegisterHistories.rejectReason]

                userStatus to rejectReason
            } else {
                userStatus to null
            }
        }
    }

    fun register(
        userId: Long,
        targetWakeUpTime: TargetWakeUpTimeDto,
        refundBankName: BankEnum,
        refundAccount: String,
        mode: ModeEnum,
    ){
        validateRegister(refundBankName, refundAccount, mode)

        transaction {
            registerUser(userId, targetWakeUpTime, refundBankName, refundAccount, mode)

            UserRegisterHistories.insert {
                it[this.user] = userId
                it[this.status] = UserRegisterStatusEnum.REQUEST
                it[this.createdAt] = LocalDateTime.now()
                it[this.updatedAt] = LocalDateTime.now()
            }
        }
    }

    fun updateRegister(
        userId: Long,
        targetWakeUpTime: TargetWakeUpTimeDto,
        refundBankName: BankEnum,
        refundAccount: String,
        mode: ModeEnum,
    ) {
        validateRegister(refundBankName, refundAccount, mode)

        transaction {
            registerUser(userId, targetWakeUpTime, refundBankName, refundAccount, mode)

            val userRegisterHistoryId = UserRegisterHistories
                .select { UserRegisterHistories.user eq userId and UserRegisterHistories.deletedAt.isNull() }
                .last()[UserRegisterHistories.id].value

            UserRegisterHistories.update({ UserRegisterHistories.id eq userRegisterHistoryId }) {
                it[this.updatedAt] = LocalDateTime.now()
            }
        }
    }

    private fun registerUser(
        userId: Long,
        targetWakeUpTime: TargetWakeUpTimeDto,
        refundBankName: BankEnum,
        refundAccount: String,
        mode: ModeEnum,
    ) {
        Users.update({ Users.id eq userId }) {
            it[this.targetWakeUpTime] = TargetWakeUpTimeEnum.toEnum(targetWakeUpTime.hour, targetWakeUpTime.minute)
            it[this.refundBankName] = refundBankName
            it[this.refundAccount] = refundAccount
            it[this.mode] = mode
            it[this.status] = UserStatusEnum.REQUEST
            it[this.updatedAt] = LocalDateTime.now()
        }
    }

    private fun validateRegister(
        refundBankName: BankEnum,
        refundAccount: String,
        mode: ModeEnum
    ) {
        if(refundBankName.value.isBlank()){
            throw MHException(ErrorCodeEnum.BANK_NAME_BLANK.code, HttpStatus.BAD_REQUEST, "Bank name is blank.")
        }

        if(refundAccount.isBlank()){
            throw MHException(ErrorCodeEnum.BANK_ACCOUNT_BLANK.code, HttpStatus.BAD_REQUEST, "Bank Account is blank.")
        }

        if(mode.value.isBlank()){
            throw MHException(ErrorCodeEnum.MODE_BLANK.code, HttpStatus.BAD_REQUEST, "Mode is blank.")
        }
    }
}