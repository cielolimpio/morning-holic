package com.morningholic.morningholicapp.services

import com.morningholic.morningholicapp.dtos.DiaryImageInfo
import com.morningholic.morningholiccommon.entities.Diaries
import com.morningholic.morningholiccommon.entities.DiaryImages
import com.morningholic.morningholiccommon.entities.UserScores
import com.morningholic.morningholiccommon.entities.Users
import com.morningholic.morningholiccommon.enums.DiaryImageTypeEnum
import com.morningholic.morningholiccommon.enums.DiaryStatusEnum
import com.morningholic.morningholiccommon.enums.DiaryTypeEnum
import com.morningholic.morningholiccommon.enums.UserStatusEnum
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.javatime.day
import org.jetbrains.exposed.sql.javatime.month
import org.jetbrains.exposed.sql.javatime.year
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ScheduleService {
    fun processAllUserScores() {
        val yesterday = LocalDateTime.now().minusDays(1)

        val diaryInfosToUpdate = transaction {
            Diaries.select {
                (Diaries.createdAt.year() eq yesterday.year) and
                        (Diaries.createdAt.month() eq yesterday.monthValue) and
                        (Diaries.createdAt.day() eq yesterday.dayOfMonth) and
                        (Diaries.status neq DiaryStatusEnum.DONE)
            }.map {
                DiaryInfoToUpdate(
                    userId = it[Diaries.user].value,
                    diaryId = it[Diaries.id].value,
                    diaryType = it[Diaries.type],
                )
            }
        }

        transaction {
            diaryInfosToUpdate.forEach { diaryInfoToUpdate ->
                val diaryImageInfos = DiaryImages
                    .select { DiaryImages.diary eq diaryInfoToUpdate.diaryId }
                    .map {
                        DiaryImageInfo(
                            imageId = it[DiaryImages.image]?.value,
                            type = it[DiaryImages.type],
                            minusScore = it[DiaryImages.minusScore],
                            createdAt = it[DiaryImages.createdAt]!!,
                        )
                    }

                val wakeUpMinusScore = diaryImageInfos.firstOrNull { it.type == DiaryImageTypeEnum.WAKE_UP }
                    ?.minusScore
                    ?:let {
                        DiaryImages.insert {
                            it[this.diary] = diaryInfoToUpdate.diaryId
                            it[this.type] = DiaryImageTypeEnum.WAKE_UP
                            it[this.minusScore] = 2
                        }
                        2
                    }

                val routineMinusScore = processRoutineDiaryImagesAndGetMinusScore(
                    diaryId = diaryInfoToUpdate.diaryId,
                    diaryType = diaryInfoToUpdate.diaryType,
                    diaryImageInfos = diaryImageInfos,
                )

                Diaries.update({ Diaries.id eq diaryInfoToUpdate.diaryId }) {
                    it[this.status] = DiaryStatusEnum.DONE
                    it[this.updatedAt] = LocalDateTime.now()
                }

                updateScore(
                    userId = diaryInfoToUpdate.userId,
                    totalMinusScore = wakeUpMinusScore + routineMinusScore
                )

            }
        }
    }

    data class DiaryInfoToUpdate(
        val userId: Long,
        val diaryId: Long,
        val diaryType: DiaryTypeEnum,
    )

    private fun processRoutineDiaryImagesAndGetMinusScore(
        diaryId: Long,
        diaryType: DiaryTypeEnum,
        diaryImageInfos: List<DiaryImageInfo>,
    ): Int {
        val routineStartDiaryImageInfo = diaryImageInfos
            .firstOrNull { it.type == DiaryImageTypeEnum.ROUTINE_START }
        val routineEndDiaryImageInfo = diaryImageInfos
            .firstOrNull { it.type == DiaryImageTypeEnum.ROUTINE_END }

        return when (diaryType) {
            DiaryTypeEnum.INDOOR -> processIndoorRoutineAndGetMinusScore(
                diaryId = diaryId,
                routineStartDiaryImageInfo = routineStartDiaryImageInfo,
                routineEndDiaryImageInfo = routineEndDiaryImageInfo,
            )
            DiaryTypeEnum.OUTDOOR -> processOutdoorRoutineAndGetMinusScore(
                diaryId = diaryId,
                routineStartDiaryImageInfo = routineStartDiaryImageInfo,
            )
        }
    }

    private fun processIndoorRoutineAndGetMinusScore(
        diaryId: Long,
        routineStartDiaryImageInfo: DiaryImageInfo?,
        routineEndDiaryImageInfo: DiaryImageInfo?,
    ): Int {
        return if (routineStartDiaryImageInfo == null) {
            // 루틴 인증 X
            DiaryImages.insert {
                it[this.diary] = diaryId
                it[this.type] = DiaryImageTypeEnum.ROUTINE_START
                it[this.minusScore] = 0
            }
            DiaryImages.insert {
                it[this.diary] = diaryId
                it[this.type] = DiaryImageTypeEnum.ROUTINE_END
                it[this.minusScore] = 2
            }
            2
        } else if (routineEndDiaryImageInfo == null) {
            // 루틴 끝 인증 X
            DiaryImages.insert {
                it[this.diary] = diaryId
                it[this.type] = DiaryImageTypeEnum.ROUTINE_END
                it[this.minusScore] = 2
            }
            2
        } else {
            // 루틴 인증 완료. get minus score
            routineEndDiaryImageInfo.minusScore
        }
    }

    private fun processOutdoorRoutineAndGetMinusScore(
        diaryId: Long,
        routineStartDiaryImageInfo: DiaryImageInfo?,
    ): Int {
        return if (routineStartDiaryImageInfo == null) {
            // 루틴 인증 X
            DiaryImages.insert {
                it[this.diary] = diaryId
                it[this.type] = DiaryImageTypeEnum.ROUTINE_START
                it[this.minusScore] = 2
            }
            2
        } else {
            // 루틴 인증 완료. get minus score
            routineStartDiaryImageInfo.minusScore
        }
    }

    private fun updateScore(
        userId: Long,
        totalMinusScore: Int,
    ) {
        val now = LocalDateTime.now()

        val currentScore = UserScores.select {
            (UserScores.user eq userId) and
                    (UserScores.year eq now.year) and (UserScores.month eq now.month.value)
        }
            .first()[UserScores.score]

        UserScores.update({
            (UserScores.user eq userId) and
                    (UserScores.year eq now.year) and (UserScores.month eq now.month.value)
        }) {
            it[this.score] = currentScore - totalMinusScore
        }
    }

    fun createNewDiaries() {
        transaction {
            val userIds = Users.slice(Users.id)
                .select { Users.status eq UserStatusEnum.ACCEPT }
                .map { it[Users.id].value }

            Diaries.batchInsert(userIds) {
                this[Diaries.user] = it
                this[Diaries.type] = DiaryTypeEnum.INDOOR
                this[Diaries.createdAt] = LocalDateTime.now()
                this[Diaries.updatedAt] = LocalDateTime.now()
            }
        }
    }
}