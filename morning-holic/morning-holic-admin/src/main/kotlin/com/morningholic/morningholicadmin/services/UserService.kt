package com.morningholic.morningholicadmin.services

import com.morningholic.morningholicadmin.dtos.TargetWakeUpTimeDto
import com.morningholic.morningholicadmin.dtos.UserInfo
import com.morningholic.morningholiccommon.entities.UserRegisterHistories
import com.morningholic.morningholiccommon.entities.UserScores
import com.morningholic.morningholiccommon.entities.Users
import com.morningholic.morningholiccommon.enums.UserRegisterStatusEnum
import com.morningholic.morningholiccommon.enums.UserStatusEnum
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.support.PageableExecutionUtils
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.function.LongSupplier

@Service
class UserService {
    fun getUserListByStatus(
        userStatus: UserStatusEnum,
        pageable: Pageable,
    ): List<UserInfo> {
        return transaction {
            Users.select { Users.status eq userStatus and Users.deletedAt.isNull() }
                .limit(n = pageable.pageSize, offset = pageable.offset)
                .orderBy(Users.id, SortOrder.DESC)
                .map {
                    UserInfo(
                        userId = it[Users.id].value,
                        name = it[Users.name],
                        phoneNumber = it[Users.phoneNumber],
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
                        createdAt = it[Users.createdAt],
                    )
                }
        }
    }

    fun getTotalUserCountByStatus(userStatus: UserStatusEnum): Long {
        return transaction {
            Users.select { Users.status eq userStatus and Users.deletedAt.isNull() }.count()
        }
    }

    fun updateUserStatus(
        userId: Long,
        userStatus: UserStatusEnum,
        rejectReason: String?,
    ) {
        transaction {
            Users.update({ Users.id eq userId }) {
                it[this.status] = userStatus
                it[this.updatedAt] = LocalDateTime.now()
            }

            val userRegisterHistoryId = UserRegisterHistories
                .select { UserRegisterHistories.user eq userId }
                .last()[UserRegisterHistories.id].value

            UserRegisterHistories.update({ UserRegisterHistories.id eq userRegisterHistoryId }) {
                it[this.status] = UserRegisterStatusEnum.from(userStatus.value)
                it[this.rejectReason] = rejectReason
                it[this.updatedAt] = LocalDateTime.now()
            }

            if (userStatus == UserStatusEnum.ACCEPT) {
                UserScores.insert {
                    it[this.user] = userId
                    it[this.year] = LocalDateTime.now().year
                    it[this.month] = LocalDateTime.now().monthValue // TODO
                    it[this.score] = 100
                }
            }
        }
    }
}