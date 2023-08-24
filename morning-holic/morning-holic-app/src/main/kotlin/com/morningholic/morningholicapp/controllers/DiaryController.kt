package com.morningholic.morningholicapp.controllers

import com.morningholic.morningholicapp.dtos.DiaryImageInfo
import com.morningholic.morningholicapp.payloads.dto.DiaryImageRequest
import com.morningholic.morningholicapp.payloads.request.InsertDiaryContentRequest
import com.morningholic.morningholicapp.payloads.request.UploadDiaryImageRequest
import com.morningholic.morningholicapp.securities.UserDetailsImpl
import com.morningholic.morningholicapp.services.DiaryService
import com.morningholic.morningholicapp.services.ImageService
import com.morningholic.morningholiccommon.enums.DiaryImageTypeEnum
import com.morningholic.morningholiccommon.enums.DiaryTypeEnum
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
@RequestMapping("/diary")
class DiaryController(
    private val imageService: ImageService,
    private val diaryService: DiaryService,
) {
    @PostMapping
    fun uploadDiaryImage(
        @RequestBody request: UploadDiaryImageRequest,
        @AuthenticationPrincipal userDetails: UserDetailsImpl
    ) {
        val diaryImageTypes = if (request.diaryType == DiaryTypeEnum.INDOOR) {
            DiaryImageTypeEnum.values().toList()
        } else {
            listOf(DiaryImageTypeEnum.WAKE_UP, DiaryImageTypeEnum.ROUTINE_START)
        }

        val diaryImageInfos = diaryImageTypes.map { diaryImageType ->
            request.diaryImages.firstOrNull { it.diaryImageType == diaryImageType }
                ?.let { diaryImageRequest ->
                    val imageId = imageService.insertImageAndGetId(
                        originalS3Path = diaryImageRequest.originalS3Path,
                        thumbnailS3Path = diaryImageRequest.thumbnailS3Path,
                    )
                    DiaryImageInfo(
                        imageId = imageId,
                        type = diaryImageType,
                        minusScore = diaryImageRequest.minusScore,
                        createdAt = diaryImageRequest.datetime,
                    )
                }
                ?: DiaryImageInfo(
                    imageId = null,
                    type = diaryImageType,
                    minusScore = getFailedMinusScore(request.diaryType, diaryImageType),
                    createdAt = LocalDateTime.now(),
                )
        }

        diaryImageInfos.forEach { diaryImageInfo ->
            diaryService.uploadDiaryImage(
                diaryId = request.diaryId,
                imageId = diaryImageInfo.imageId,
                diaryImageType = diaryImageInfo.type,
                datetime = diaryImageInfo.createdAt,
                minusScore = diaryImageInfo.minusScore,
            )
        }

        diaryService.updateOrInsertDiaryContent(
            diaryId = request.diaryId,
            content = request.diaryContent,
            datetime = request.datetime,
        )

        diaryService.totalizeDiary(
            userId = userDetails.userId,
            diaryId = request.diaryId,
            diaryType = request.diaryType,
        )
    }

    private fun getFailedMinusScore(
        diaryType: DiaryTypeEnum,
        diaryImageType: DiaryImageTypeEnum,
    ): Int {
        return if (diaryType == DiaryTypeEnum.INDOOR && diaryImageType == DiaryImageTypeEnum.ROUTINE_START) 0
        else 2
    }

    @PutMapping("/content")
    fun insertDiaryContent(
        @RequestBody request: InsertDiaryContentRequest,
        @AuthenticationPrincipal userDetails: UserDetailsImpl,
    ) {
        diaryService.updateOrInsertDiaryContent(
            diaryId = request.diaryId,
            content = request.diaryContent,
        )
    }
}