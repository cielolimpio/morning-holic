package com.morningholic.morningholiccommon.entities

import com.morningholic.morningholiccommon.enums.DiaryStatusEnum
import com.morningholic.morningholiccommon.enums.DiaryTypeEnum
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.javatime.datetime

object Diaries: LongIdTable("diaries", "id") {
    val user = reference("user_id", Users)
    val type = enumerationByName("type", 20, DiaryTypeEnum::class)
    val status = enumerationByName("status", 20, DiaryStatusEnum::class).default(DiaryStatusEnum.INITIAL)
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at")
    val deletedAt = datetime("deleted_at").nullable()
}