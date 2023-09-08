package com.morningholic.morningholicapp.payloads.dto

import com.morningholic.morningholiccommon.enums.DiaryImageTypeEnum
import java.time.LocalDateTime

data class DiaryImageRequest(
    val originalS3Path: String,
    val thumbnailS3Path: String,
    val diaryImageType: DiaryImageTypeEnum,
    val datetime: LocalDateTime,
    val timezone: String,
    val timezoneOffset: Int,
    val minusScore: Int,
)
