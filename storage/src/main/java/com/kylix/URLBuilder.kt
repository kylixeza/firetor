package com.kylix

object URLBuilder {

    internal fun initPath() = ""

    internal fun String.reference(ref: String) = "$this$ref%2F"

    internal fun String.getDownloadUrl(fileName: String) = run {
        if(this.isEmpty()) {
            return@run "https://firebasestorage.googleapis.com/v0/b/${Configuration.storageBucket}/o/$fileName?alt=media"
        } else {
            return@run "https://firebasestorage.googleapis.com/v0/b/${Configuration.storageBucket}/o/$this$fileName?alt=media"
        }
    }

}