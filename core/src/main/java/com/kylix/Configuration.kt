package com.kylix

import com.google.firebase.FirebaseOptions

object Configuration {

    internal var adminKeyFileName: String? = null
    internal var storageBucket: String? = null
    internal var basicConfiguration: FirebaseOptions.Builder.() -> FirebaseOptions.Builder? = { null }

    fun setAdminKey(fileName: String) {
        adminKeyFileName = fileName
    }

    fun enableFirebaseStorage(bucketUrl: String) {
        storageBucket = bucketUrl
    }

    fun getStorageBucket() = storageBucket

    fun setBasicConfiguration(config: FirebaseOptions.Builder.() -> FirebaseOptions.Builder?) {
        basicConfiguration = config
    }

}