package com.morningholic.morningholicapp.utils

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.S3Object
import com.amazonaws.services.s3.transfer.TransferManager
import com.morningholic.morningholicapp.configs.AWSConfig
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Import
import org.springframework.stereotype.Component
import java.io.ByteArrayInputStream
import java.util.*

@Component
@Import(AWSConfig::class)
class S3Util(
    private val s3Client: AmazonS3,
    private val s3Multipart: TransferManager,
) {
    @Value("\${aws.s3.bucket}")
    lateinit var bucketName: String

    fun getS3Object(s3Path: String): S3Object {
        return s3Client.getObject(bucketName, s3Path)
    }

    fun uploadToS3(
        s3Key: String,
        byteArray: ByteArray,
    ) {
        val objectMetadata = ObjectMetadata()
        objectMetadata.contentLength = byteArray.size.toLong()
        val byteArrayInputStream = ByteArrayInputStream(byteArray)
        val upload = s3Multipart.upload(bucketName, s3Key, byteArrayInputStream, objectMetadata)
        upload.waitForCompletion()
    }
}