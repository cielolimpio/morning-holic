package com.morningholic.morningholicapp.services

import com.morningholic.morningholicapp.utils.FileUtil
import com.morningholic.morningholicapp.utils.S3Util
import com.morningholic.morningholiccommon.entities.Images
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Service
import java.io.ByteArrayOutputStream
import java.io.File
import java.time.LocalDateTime
import java.util.*
import javax.imageio.ImageIO

@Service
class ImageService(
    private val s3Util: S3Util,
) {
    fun insertImageAndGetId(
        originalS3Path: String,
        thumbnailS3Path: String,
    ): Long {
        return transaction {
            val s3Object = s3Util.getS3Object(originalS3Path)
            val uuid = UUID.randomUUID().toString()
            val extension = s3Object.key.substringAfterLast(".").lowercase()
            val originalFile = File.createTempFile(uuid, ".$extension")
                .also { it.writeBytes(s3Object.objectContent.delegateStream.readAllBytes()) }

            val thumbnailImage = FileUtil.createThumbnailImage(originalFile)

            val thumbOutput = ByteArrayOutputStream()
            ImageIO.write(thumbnailImage, "jpg", thumbOutput)
            val resizedFileBytes = thumbOutput.toByteArray()

            val thumbnailImageS3Key =
                "thumbnails/" + s3Object.key.substringAfter("images/").substringBeforeLast('.') + "_thumb.jpg"

            s3Util.uploadToS3(
                s3Key = thumbnailImageS3Key,
                byteArray = resizedFileBytes,
            )
            originalFile.delete()

            Images.insertAndGetId {
                it[this.originalS3Path] = originalS3Path
                it[this.thumbnailS3Path] = thumbnailS3Path
                it[this.createdAt] = LocalDateTime.now()
            }.value
        }
    }
}