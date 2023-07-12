package com.morningholic.morningholicapp.services

import com.morningholic.morningholicapp.enums.ErrorCodeEnum
import com.morningholic.morningholiccommon.entities.Users
import com.morningholic.morningholiccommon.enums.BankEnum
import com.morningholic.morningholiccommon.enums.ModeEnum
import com.morningholic.morningholiccommon.enums.UserStatusEnum
import com.morningholic.morningholiccommon.exception.MHException
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import java.time.LocalDateTime

@Service
class UserService {
    fun register(
        targetWakeUpTime: LocalDateTime,
        refundBankName: BankEnum,
        refundAccount: String,
        mode: ModeEnum,
        userId: Long
    ){
        validateRegister(refundBankName, refundAccount, mode)

        return transaction {
            Users.update({ Users.id eq userId }) {
                it[this.targetWakeUpTime] = targetWakeUpTime
                it[this.refundBankName] = refundBankName.value
                it[this.refundAccount] = refundAccount
                it[this.mode] = mode
                it[this.status] = UserStatusEnum.REQUEST
                it[this.updatedAt] = LocalDateTime.now()
            }
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