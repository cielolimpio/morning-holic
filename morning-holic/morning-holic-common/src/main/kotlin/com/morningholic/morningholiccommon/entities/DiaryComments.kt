package com.morningholic.morningholiccommon.entities

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.javatime.datetime

object DiaryComments: LongIdTable("diary_comments", "id") {
    val diary = reference("diary_id", Diaries)
    val user = reference("user_id", Users)
    val comment = text("comment")
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at")
    val deletedAt = datetime("deleted_at").nullable()
}