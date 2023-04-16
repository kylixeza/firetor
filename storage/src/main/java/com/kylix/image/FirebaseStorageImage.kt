package com.kylix.image

import com.drew.imaging.ImageMetadataReader
import com.drew.metadata.exif.ExifIFD0Directory
import com.google.firebase.cloud.StorageClient
import com.kylix.util.URLBuilder
import com.kylix.util.URLBuilder.getDownloadUrl
import com.kylix.util.URLBuilder.reference
import io.ktor.http.content.*
import java.io.ByteArrayInputStream

object FirebaseStorageImage {

    private val bucket = StorageClient.getInstance().bucket()

    fun PartData.FileItem.uploadImage(
        path: String? = null,
        fileExtension: ImageExtension = ImageExtension.ORIGINAL_FILE_EXTENSION,
        preprocessing: ImagePreprocessing.(ByteArray) -> ByteArray = { it }
    ) = run {
        val fileBytes = streamProvider().readBytes()
        val isRawImagePortrait = fileBytes.isPortraitImage()
        val originalFileName = this.originalFileName

        val imagePipeline = ImagePreprocessing(fileExtension, isRawImagePortrait, originalFileName)
        val processedImage = imagePipeline.preprocessing(fileBytes)
        val normalizedImage = if (isRawImagePortrait) {
            imagePipeline.run { processedImage.rotate(90.0) }
        } else {
            processedImage
        }

        val fileName = imagePipeline.getFileName()

        val contentType = when (fileExtension) {
            ImageExtension.ORIGINAL_FILE_EXTENSION -> "image/${originalFileName?.split(".")?.last()}"
            else -> "image/${fileExtension.extension}"
        }

        if (path == null) {
            bucket.create(fileName, normalizedImage, contentType)
            return@run URLBuilder.initPath().getDownloadUrl(fileName)
        } else {
            bucket.create("$path/$fileName", normalizedImage, contentType)
            var url = URLBuilder.initPath()
            val paths = path.split("/")
            paths.forEach { path ->
                url = url.reference(path)
            }
            return@run url.getDownloadUrl(fileName)
        }
    }

    private fun ByteArray.isPortraitImage(): Boolean = run {
        val inputStream = ByteArrayInputStream(this)
        val metadata = ImageMetadataReader.readMetadata(inputStream)
        val directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory::class.java)
        val orientation = directory?.getInt(ExifIFD0Directory.TAG_ORIENTATION) ?: 0
        return orientation == 6 || orientation == 8
    }

}