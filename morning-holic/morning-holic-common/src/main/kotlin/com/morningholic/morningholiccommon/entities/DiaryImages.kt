package com.morningholic.morningholiccommon.entities

import com.morningholic.morningholiccommon.enums.DiaryImageTypeEnum
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.javatime.datetime

object DiaryImages: LongIdTable("diary_images", "id") {
    val diary = reference("diary_id", Diaries)
    val image = reference("image_id", Images)
    val type = enumerationByName("type", 20, DiaryImageTypeEnum::class)
    val minusScore = integer("minus_score").default(0)
    val createdAt = datetime("created_at")

}