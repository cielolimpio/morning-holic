package com.morningholic.morningholicapp.services

import com.morningholic.morningholiccommon.entities.*
import com.morningholic.morningholiccommon.enums.DiaryImageTypeEnum
import com.morningholic.morningholiccommon.enums.DiaryTypeEnum
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime

@Service
class DiaryService {
    private val log = LoggerFactory.getLogger(DiaryService::class.java)

    fun getDiaryId(userId: Long): Long {
        return transaction {
            Diaries.select { Diaries.user eq userId }.lastOrNull()
                ?.let {
                    if (it[Diaries.createdAt].dayOfMonth == LocalDateTime.now().dayOfMonth)
                        it[Diaries.id].value
                    else {
                        createDiaryAndGetId(userId)
                    }
                }
                ?: createDiaryAndGetId(userId)
        }
    }

    private fun createDiaryAndGetId(userId: Long): Long {
        return Diaries.insertAndGetId {
            it[this.user] = userId
            it[this.type] = DiaryTypeEnum.INDOOR
            it[this.createdAt] = LocalDateTime.now()
            it[this.updatedAt] = LocalDateTime.now()
        }.value
    }

    fun uploadDiaryImage(
        diaryId: Long,
        imageId: Long?,
        diaryImageType: DiaryImageTypeEnum,
        datetime: LocalDateTime?, // 앱에서 찍은 시간 UTC 기준
        timezone: String?,
        timezoneOffset: Int?,
        minusScore: Int, // 앱에서 디바이스 시간 기준으로 책정
    ) {
        transaction {
            DiaryImages.insert {
                it[this.diary] = diaryId
                it[this.image] = imageId
                it[this.type] = diaryImageType
                it[this.minusScore] = minusScore
                it[this.datetime] = datetime
                it[this.timezone] = timezone
                it[this.timezoneOffset] = timezoneOffset
                it[this.createdAt] = ZonedDateTime.now(ZoneOffset.UTC).toLocalDateTime()
            }
        }
    }

    fun updateOrInsertDiaryContent(
        diaryId: Long,
        content: String,
    ) {
        transaction {
            DiaryContents.select { DiaryContents.diary eq diaryId }.firstOrNull()
                ?.let {
                    DiaryContents.update({ DiaryContents.diary eq diaryId }) {
                        it[this.content] = content
                        it[this.updatedAt] = LocalDateTime.now()
                    }
                }
                ?: {
                    DiaryContents.insert {
                        it[this.diary] = diaryId
                        it[this.content] = content
                        it[this.createdAt] = LocalDateTime.now()
                        it[this.updatedAt] = LocalDateTime.now()
                    }
                }
        }
    }

    fun totalizeDiary(
        userId: Long,
        diaryId: Long,
        diaryType: DiaryTypeEnum,

    ) {
        transaction {
            Diaries.update({ Diaries.id eq diaryId }) {
                it[this.type] = diaryType
                it[this.updatedAt] = LocalDateTime.now()
            }

            updateScore(
                userId = userId,
                diaryId = diaryId,
            )
        }
    }

    private fun updateScore(
        userId: Long,
        diaryId: Long,
    ) {
        val now = LocalDateTime.now()

        val currentScore = UserScores.select {
            (UserScores.user eq userId) and
                    (UserScores.year eq now.year) and (UserScores.month eq now.month.value)
        }
            .first()[UserScores.score]

        val totalMinusScore =
            DiaryImages.select { DiaryImages.diary eq diaryId }.sumOf { it[DiaryImages.minusScore] }

        UserScores.update({
            (UserScores.user eq userId) and
                    (UserScores.year eq now.year) and (UserScores.month eq now.month.value)
        }) {
            it[this.score] = currentScore - totalMinusScore
        }
    }
}