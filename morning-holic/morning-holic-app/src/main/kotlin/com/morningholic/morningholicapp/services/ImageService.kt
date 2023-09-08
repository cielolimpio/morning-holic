package com.morningholic.morningholicapp.services

import com.morningholic.morningholicapp.utils.S3Util
import com.morningholic.morningholiccommon.entities.Images
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ImageService(
    private val s3Util: S3Util,
) {
    fun insertImageAndGetId(
        originalS3Path: String,
        thumbnailS3Path: String,
    ): Long {
        return transaction {
            Images.insertAndGetId {
                it[this.originalS3Path] = originalS3Path
                it[this.thumbnailS3Path] = thumbnailS3Path
                it[this.createdAt] = LocalDateTime.now()
            }.value
        }
    }

    fun getPresignedUrl(s3Path: String): String {
        return s3Util.generatePresignedUrl(s3Path)
    }
}