package com.morningholic.morningholicapp.services

import com.morningholic.morningholiccommon.entities.*
import com.morningholic.morningholiccommon.enums.DiaryImageTypeEnum
import com.morningholic.morningholiccommon.enums.DiaryTypeEnum
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class DiaryService {
    fun uploadDiaryImage(
        diaryId: Long,
        imageId: Long?,
        diaryImageType: DiaryImageTypeEnum,
        datetime: LocalDateTime, // 앱에서 찍은 시간
        minusScore: Int, // 앱에서 디바이스 시간 기준으로 책정
    ) {
        transaction {
            DiaryImages.insert {
                it[this.diary] = diaryId
                it[this.image] = imageId
                it[this.type] = diaryImageType
                it[this.minusScore] = minusScore
                it[this.createdAt] = datetime
            }
        }
    }

    fun updateOrInsertDiaryContent(
        diaryId: Long,
        content: String,
        datetime: LocalDateTime = LocalDateTime.now(),
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
                        it[this.createdAt] = datetime
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