package com.morningholic.morningholiccommon.entities

import com.morningholic.morningholiccommon.enums.UserRegisterStatusEnum
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.javatime.datetime

object UserRegisterHistories: LongIdTable("user_register_histories", "id") {
    val user = reference("user_id", Users)
    val status = enumerationByName("status", 20, UserRegisterStatusEnum::class)
    val rejectReason = varchar("reject_reason", 300).nullable()
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at")
    val deletedAt = datetime("deleted_at").nullable()
}