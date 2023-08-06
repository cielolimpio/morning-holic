package com.morningholic.morningholiccommon.entities

import com.morningholic.morningholiccommon.enums.*
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.javatime.datetime

object Users: LongIdTable("users", "id") {
    val name = varchar("name", 50)
    val phoneNumber = varchar("phone_number", 50)
    val password = varchar("password", 200)
    val role = enumerationByName("role", 20, RoleEnum::class)
    val nickname = varchar("nickname", 50)
    val targetWakeUpTime = enumerationByName("target_wake_up_time", 20, TargetWakeUpTimeEnum::class).nullable()
    val refundBankName = enumerationByName("refund_bank_name", 100, BankEnum::class).nullable()
    val refundAccount = varchar("refund_account", 100).nullable()
    val mode = enumerationByName("mode", 20, ModeEnum::class).nullable()
    val status = enumerationByName("status", 20, UserStatusEnum::class)
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at")
    val deletedAt = datetime("deleted_at").nullable()
}