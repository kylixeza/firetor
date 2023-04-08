package com.kylix

/**
 * @author Kylix Eza Saputra
 */

internal fun<T> checkNotNull(value: T?, message: String) {
    if (value == null) {
        throw IllegalArgumentException(message)
    }
}

internal fun checkIsBucketUrlRight(url: String?) {
    if (url == null) {
        return
    }
    if (!url.endsWith(".appspot.com")) {
        throw IllegalArgumentException("Bucket URL must end with .appspot.com")
    }
}

internal fun checkIsFirebaseStorageEnabled(bucketUrl: String?): Boolean = bucketUrl != null