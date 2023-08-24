package com.morningholic.morningholiccommon.entities

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.javatime.datetime

object DiaryContents: LongIdTable("diary_contents", "id") {
    val diary = reference("diary_id", Diaries)
    val content = text("content")
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at")
}