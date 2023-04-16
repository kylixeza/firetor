package com.kylix.document

import com.google.firebase.cloud.StorageClient
import com.kylix.util.URLBuilder
import com.kylix.util.URLBuilder.getDownloadUrl
import com.kylix.util.URLBuilder.reference
import io.ktor.http.content.*

object FirebaseStorageDocument {

    private val bucket = StorageClient.getInstance().bucket()

    fun PartData.FileItem.uploadDocument(
        path: String? = null,
        preprocess: DocumentPreprocessing.(ByteArray) -> ByteArray = { it }
    ) = run {
        val fileBytes = streamProvider().readBytes()
        val originalFileName = this.originalFileName
        val originalExtension = originalFileName?.substringAfterLast(".") ?: "txt"

        val documentPreprocessing = DocumentPreprocessing(originalFileName)
        val preprocessedDocument = documentPreprocessing.preprocess(fileBytes)
        val fileName = documentPreprocessing.getFileName()
        val contentType = originalExtension.getContentType()

        if (path == null) {
            bucket.create(fileName, preprocessedDocument, contentType)
            return@run URLBuilder.initPath().getDownloadUrl(fileName)
        } else {
            bucket.create("$path/$fileName", preprocessedDocument, contentType)
            var url = URLBuilder.initPath()
            val paths = path.split("/")
            paths.forEach { path ->
                url = url.reference(path)
            }
            return@run url.getDownloadUrl(fileName)
        }
    }

    private fun String.getContentType() = run {
        when(this) {
            DocumentContentType.DOCX.extension -> DocumentContentType.DOCX.contentType
            DocumentContentType.DOC.extension -> DocumentContentType.DOC.contentType
            DocumentContentType.PDF.extension -> DocumentContentType.PDF.contentType
            DocumentContentType.XLSX.extension -> DocumentContentType.XLSX.contentType
            DocumentContentType.XLS.extension -> DocumentContentType.XLS.contentType
            DocumentContentType.PPTX.extension -> DocumentContentType.PPTX.contentType
            DocumentContentType.PPT.extension -> DocumentContentType.PPT.contentType
            DocumentContentType.TXT.extension -> DocumentContentType.TXT.contentType
            else -> DocumentContentType.TXT.contentType
        }
    }

}