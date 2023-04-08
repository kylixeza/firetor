package com.kylix

/**
 * This class is used to build the URL
 * @author Kylix Eza Saputra
 */
object URLBuilder {

    internal fun initPath() = ""

    internal fun String.reference(ref: String) = "$this$ref%2F"

    internal fun String.getDownloadUrl(fileName: String) = run {
        if(this.isEmpty()) {
            return@run "https://firebasestorage.googleapis.com/v0/b/${Configuration.getStorageBucket()}/o/$fileName?alt=media"
        } else {
            return@run "https://firebasestorage.googleapis.com/v0/b/${Configuration.getStorageBucket()}/o/$this$fileName?alt=media"
        }
    }

}