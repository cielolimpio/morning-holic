package com.morningholic.morningholiccommon.entities

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.javatime.datetime

object Images: LongIdTable("images", "id") {
    val originalS3Path = varchar("original_s3_path", 200)
    val thumbnailS3Path = varchar("thumbnail_s3_path", 200)
    val createdAt = datetime("created_at")
}