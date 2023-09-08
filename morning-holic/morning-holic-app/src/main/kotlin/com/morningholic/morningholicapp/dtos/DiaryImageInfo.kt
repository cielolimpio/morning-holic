package com.morningholic.morningholicapp.dtos

import com.morningholic.morningholiccommon.enums.DiaryImageTypeEnum
import java.time.LocalDateTime

data class DiaryImageInfo(
    val imageId: Long?,
    val type: DiaryImageTypeEnum,
    val minusScore: Int,
    val datetime: LocalDateTime?,
    val timezone: String?,
    val timezoneOffset: Int?,
)
