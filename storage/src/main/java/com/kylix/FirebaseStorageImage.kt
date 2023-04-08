package com.kylix

import com.aventrix.jnanoid.jnanoid.NanoIdUtils
import com.google.firebase.cloud.StorageClient
import com.kylix.URLBuilder.getDownloadUrl
import com.kylix.URLBuilder.reference
import io.ktor.http.content.*

/**
 * This class is used to upload image to firebase storage
 * @author Kylix Eza Saputra
 */
object FirebaseStorageImage {

    private val bucket = StorageClient.getInstance().bucket()

    /**
     * This function is used to upload image to firebase storage
     * @param path the path where the image will be stored
     * @param fileExtension the extension of the image
     * @param preprocessing the preprocessing function
     * @return the download url of the image
     */
    fun PartData.FileItem.uploadImage(
        path: String? = null,
        fileExtension: ImageExtension = ImageExtension.JPG,
        preprocessing: ImagePreprocessing.(ByteArray) -> ByteArray = { it }
    ) = run {
        val fileBytes = streamProvider().readBytes()
        val imagePipeline = ImagePreprocessing(fileExtension)
        val processedImage = imagePipeline.preprocessing(fileBytes)
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