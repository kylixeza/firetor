package com.kylix

fun<T> checkNotNull(value: T?, message: String) {
    if (value == null) {
        throw IllegalArgumentException(message)
    }
}

fun checkIsBucketUrlRight(url: String?) {
    if (url == null) {
        return
    }
    if (!url.endsWith(".appspot.com")) {
        throw IllegalArgumentException("Bucket URL must end with .appspot.com")
    }
}

fun checkIsFirebaseStorageEnabled(bucketUrl: String?): Boolean = bucketUrl != null