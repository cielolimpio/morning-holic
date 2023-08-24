package com.morningholic.morningholicapp.payloads.request

import com.morningholic.morningholicapp.payloads.dto.DiaryImageRequest
import com.morningholic.morningholiccommon.enums.DiaryTypeEnum
import java.time.LocalDateTime

data class UploadDiaryImageRequest(
    val diaryId: Long,
    val diaryType: DiaryTypeEnum,
    val diaryImages: List<DiaryImageRequest>,
    val diaryContent: String,
    val datetime: LocalDateTime,
)
