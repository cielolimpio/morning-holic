package com.morningholic.morningholiccommon.entities

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.javatime.datetime

object DiaryLikes: LongIdTable("diary_likes", "id") {
    val diary = reference("diary_id", Diaries)
    val user = reference("user_id", Users)
    val createdAt = datetime("created_at")
}