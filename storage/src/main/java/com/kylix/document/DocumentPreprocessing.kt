package com.kylix.document

import com.aventrix.jnanoid.jnanoid.NanoIdUtils

class DocumentPreprocessing(
    private val originalFileName: String?
) {

    private val extension = originalFileName?.substringAfterLast(".") ?: "txt"

    private var fileName = NanoIdUtils.randomNanoId() + ".$extension"

    fun getFileName(): String = fileName

    fun ByteArray.renameWithOriginalName(includeTimeStamp: Boolean = false): ByteArray {
        fileName = originalFileName?.substringBeforeLast(".") + if (includeTimeStamp) "_${System.currentTimeMillis()}.$extension" else ".$extension"
        return this
    }

    fun ByteArray.rename(customName: String, includeTimeStamp: Boolean = false): ByteArray {
        fileName = customName + if (includeTimeStamp) "_${System.currentTimeMillis()}.$extension" else ".$extension"
        return this
    }

    fun ByteArray.renameWithRandomId(includeTimeStamp: Boolean = false): ByteArray {
        fileName = NanoIdUtils.randomNanoId() + if (includeTimeStamp) "_${System.currentTimeMillis()}.$extension" else ".$extension"
        return this
    }

}