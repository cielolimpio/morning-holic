package com.morningholic.morningholicapp.configs

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.amazonaws.services.s3.transfer.TransferManager
import com.amazonaws.services.s3.transfer.TransferManagerBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.Executors

@Configuration
class AWSConfig(
    @Value("\${aws.access-key}") private val accessKey: String,
    @Value("\${aws.secret-key}") private val secretKey: String,
) {
    @Bean("s3Client")
    fun s3Client(): AmazonS3 {
        return AmazonS3ClientBuilder
            .standard()
            .withCredentials(AWSStaticCredentialsProvider(BasicAWSCredentials(accessKey, secretKey)))
            .withRegion(Regions.AP_NORTHEAST_2)
            .build()
    }

    @Bean("s3Multipart")
    fun s3Multipart(): TransferManager {
        return TransferManagerBuilder.standard()
            .withS3Client(s3Client())
            .withMultipartUploadThreshold((5 * 1024 * 1025).toLong())
            .withMinimumUploadPartSize((5 * 1024 * 1025).toLong())
            .withExecutorFactory { Executors.newFixedThreadPool(10) }
            .build()
    }
}