package com.morningholic.morningholiccommon.entities

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.javatime.datetime

object DiaryCommentReplies: LongIdTable("diary_comment_replies", "id") {
    val diaryComment = reference("diary_comment_id", DiaryComments)
    val user = reference("user_id", Users)
    val reply = text("reply")
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at")
    val deletedAt = datetime("deleted_at").nullable()
}