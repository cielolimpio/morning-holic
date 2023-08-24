package com.morningholic.morningholicapp.utils

import com.drew.imaging.ImageMetadataReader
import com.drew.metadata.exif.ExifIFD0Directory
import org.imgscalr.Scalr
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*
import javax.imageio.ImageIO
import kotlin.math.sqrt

object FileUtil {
    private const val THUMBNAIL_SIZE_BYTES = 100 * 1024

    fun createThumbnailImage(file: File): BufferedImage {
        val metadata = ImageMetadataReader.readMetadata(file)
        val exifIFD0Dir = metadata.getFirstDirectoryOfType(ExifIFD0Directory::class.java)

        val orientation =
            if (exifIFD0Dir != null && exifIFD0Dir.containsTag(ExifIFD0Directory.TAG_ORIENTATION)) {
                val orientation = exifIFD0Dir.getInt(ExifIFD0Directory.TAG_ORIENTATION)
                if (orientation in 1..8) orientation
                else 1
            } else 1

        val img = ImageIO.read(file)
        val rotatedImg = rotateImage(img, orientation)
        val shrinkRatio = sqrt(file.readBytes().size / THUMBNAIL_SIZE_BYTES.toDouble()).toInt()
        return if (shrinkRatio < 1) {
            rotatedImg
        } else {
            Scalr.resize(
                rotatedImg,
                rotatedImg.width / shrinkRatio,
                rotatedImg.height / shrinkRatio,
                Scalr.OP_ANTIALIAS
            )
        }
    }

    private fun rotateImage(img: BufferedImage, orientation: Int): BufferedImage {
        val rotation = when (orientation) {
            2 -> Scalr.Rotation.FLIP_HORZ
            3 -> Scalr.Rotation.CW_180
            4 -> Scalr.Rotation.FLIP_VERT
            5 -> return Scalr.rotate(
                Scalr.rotate(img, Scalr.Rotation.CW_270),
                Scalr.Rotation.FLIP_VERT
            )
            6 -> Scalr.Rotation.CW_90
            7 -> return Scalr.rotate(
                Scalr.rotate(img, Scalr.Rotation.CW_270),
                Scalr.Rotation.FLIP_HORZ
            )
            8 -> Scalr.Rotation.CW_270
            else -> return img
        }
        return Scalr.rotate(img, rotation)
    }
}