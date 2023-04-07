package com.kylix

import com.aventrix.jnanoid.jnanoid.NanoIdUtils
import com.google.firebase.cloud.StorageClient
import com.kylix.URLBuilder.getDownloadUrl
import com.kylix.URLBuilder.reference
import io.ktor.http.content.*

object FirebaseStorageImage {

    private val bucket = StorageClient.getInstance().bucket()

    fun PartData.FileItem.uploadImage(
        path: String? = null,
        fileExtension: ImageExtension = ImageExtension.JPG,
        pipeline: ImagePreprocessing.(ByteArray) -> ByteArray = { it }
    ) = run {
        val fileBytes = streamProvider().readBytes()
        val imagePipeline = ImagePreprocessing(fileExtension)
        val processedImage = imagePipeline.pipeline(fileBytes)
        val fileName = NanoIdUtils.randomNanoId() + "." + fileExtension.extension
        if (path == null) {
            bucket.create(fileName, processedImage, "image/${fileExtension.extension}")
            return@run URLBuilder.initPath().getDownloadUrl(fileName)
        } else {
            bucket.create("$path/$fileName", processedImage, "image/${fileExtension.extension}")
            var url = URLBuilder.initPath()
            val paths = path.split("/")
            paths.forEach { path ->
                url = url.reference(path)
            }
            return@run url.getDownloadUrl(fileName)
        }
    }

}