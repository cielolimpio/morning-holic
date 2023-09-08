package com.morningholic.morningholicapp.controllers

import com.morningholic.morningholicapp.services.ImageService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/image")
class ImageController(
    private val imageService: ImageService,
) {
    @GetMapping("/presigned-url")
    fun getPresignedUrl(
        @RequestParam("s3Path") s3Path: String,
    ): String {
        val presignedUrl = imageService.getPresignedUrl(s3Path)
        return presignedUrl
    }
}