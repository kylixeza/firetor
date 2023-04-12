package com.kylix

import com.aventrix.jnanoid.jnanoid.NanoIdUtils
import com.google.firebase.cloud.StorageClient
import com.kylix.URLBuilder.getDownloadUrl
import com.kylix.URLBuilder.reference
import io.ktor.http.content.*
import java.io.ByteArrayInputStream
import javax.imageio.ImageIO

object FirebaseStorageImage {

    private val bucket = StorageClient.getInstance().bucket()

    fun PartData.FileItem.uploadImage(
        path: String? = null,
        fileExtension: ImageExtension = ImageExtension.JPG,
        preprocessing: ImagePreprocessing.(ByteArray) -> ByteArray = { it }
    ) = run {
        val fileBytes = streamProvider().readBytes()
        val isRawImagePortrait = this.isPortraitImage()

        val imagePipeline = ImagePreprocessing(fileExtension)
        val processedImage = imagePipeline.preprocessing(fileBytes)
        val normalizedImage = if (isRawImagePortrait) {
            imagePipeline.run { processedImage.rotate(90.0) }
        } else {
            processedImage
        }

        val fileName = NanoIdUtils.randomNanoId() + "." + fileExtension.extension
        if (path == null) {
            bucket.create(fileName, normalizedImage, "image/${fileExtension.extension}")
            return@run URLBuilder.initPath().getDownloadUrl(fileName)
        } else {
            bucket.create("$path/$fileName", normalizedImage, "image/${fileExtension.extension}")
            var url = URLBuilder.initPath()
            val paths = path.split("/")
            paths.forEach { path ->
                url = url.reference(path)
            }
            return@run url.getDownloadUrl(fileName)
        }
    }

    private fun PartData.FileItem.isPortraitImage(): Boolean = run {
        val inputStream = ByteArrayInputStream(this.streamProvider().readBytes())
        val image = ImageIO.read(inputStream)
        image.height > image.width
    }

}