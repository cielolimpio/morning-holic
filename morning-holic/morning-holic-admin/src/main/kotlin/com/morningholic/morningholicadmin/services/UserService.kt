package com.morningholic.morningholicadmin.services

import com.morningholic.morningholicadmin.dtos.UserInfo
import com.morningholic.morningholiccommon.entities.Users
import com.morningholic.morningholiccommon.enums.UserStatusEnum
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
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
                        targetWakeUpTime = it[Users.targetWakeUpTime],
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
        userStatus: UserStatusEnum
    ) {
        transaction {
            Users.update({ Users.id eq userId }) {
                it[status] = userStatus
                it[updatedAt] = LocalDateTime.now()
            }
        }
    }
}