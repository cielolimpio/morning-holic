package com.morningholic.morningholicapp.payloads.request

data class InsertDiaryContentRequest(
    val diaryId: Long,
    val diaryContent: String,
)
