package com.kylix.document

import com.aventrix.jnanoid.jnanoid.NanoIdUtils

/**
 * This class is used to preprocess the document before it is stored in the database
 * @author Kylix Eza Saputra
 */
class DocumentPreprocessing(
    private val originalFileName: String?
) {

    private val extension = originalFileName?.substringAfterLast(".") ?: "txt"

    private var fileName = NanoIdUtils.randomNanoId() + ".$extension"

    internal fun getFileName(): String = fileName

    /**
     * This function is used to rename the file with the original name
     * @param includeTimeStamp is used to include the timestamp in the file name
     * @return ByteArray
     * @author Kylix Eza Saputra
     */
    fun ByteArray.renameWithOriginalName(includeTimeStamp: Boolean = false): ByteArray {
        fileName = originalFileName?.substringBeforeLast(".") + if (includeTimeStamp) "_${System.currentTimeMillis()}.$extension" else ".$extension"
        return this
    }

    /**
     * This function is used to rename the file with the custom name
     * @param customName is used to set the custom name
     * @param includeTimeStamp is used to include the timestamp in the file name
     * @return ByteArray
     * @author Kylix Eza Saputra
     */
    fun ByteArray.rename(customName: String, includeTimeStamp: Boolean = false): ByteArray {
        fileName = customName + if (includeTimeStamp) "_${System.currentTimeMillis()}.$extension" else ".$extension"
        return this
    }

    /**
     * This function is used to rename the file with the random id
     * @param includeTimeStamp is used to include the timestamp in the file name
     * @return ByteArray
     */
    fun ByteArray.renameWithRandomId(includeTimeStamp: Boolean = false): ByteArray {
        fileName = NanoIdUtils.randomNanoId() + if (includeTimeStamp) "_${System.currentTimeMillis()}.$extension" else ".$extension"
        return this
    }

}