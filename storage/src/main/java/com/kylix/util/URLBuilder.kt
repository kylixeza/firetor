package com.kylix.util

import com.kylix.Configuration

object URLBuilder {

    internal fun initPath() = ""

    internal fun String.reference(ref: String) = "$this$ref%2F"

    internal fun String.getDownloadUrl(fileName: String) = run {
        val standardizedName = fileName.replace(" ", "%20")
        if(this.isEmpty()) {
            return@run "https://firebasestorage.googleapis.com/v0/b/${Configuration.getStorageBucket()}/o/$standardizedName?alt=media"
        } else {
            return@run "https://firebasestorage.googleapis.com/v0/b/${Configuration.getStorageBucket()}/o/$this$standardizedName?alt=media"
        }
    }

}