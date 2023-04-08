package com.kylix

import com.google.firebase.FirebaseOptions

/**
 * The `Configuration` object is used to configure the Firebase SDK.
 * @author Kylix Eza Saputra
 */
object Configuration {

    internal var adminKeyFileName: String? = null
    internal var storageBucket: String? = null
    internal var basicConfiguration: FirebaseOptions.Builder.() -> FirebaseOptions.Builder? = { null }

    /**
     * The function sets the admin key file name.
     *
     * @param fileName The `fileName` parameter is a string that represents the name of a file that contains the admin key.
     * The `setAdminKey` function sets the value of the `adminKeyFileName` variable to the value of the `fileName`
     * parameter.
     */
    fun setAdminKey(fileName: String) {
        adminKeyFileName = fileName
    }

    /**
     * The function enables Firebase Storage by setting the storage bucket URL.
     *
     * @param bucketUrl The `bucketUrl` parameter is a string that represents the URL of the Firebase Storage bucket that
     * you want to enable in your application. This URL is typically in the format `gs://<your-bucket-name>.appspot.com/`.
     * But you just need to pass <your-bucket-name>.appspot.com as the parameter.
     */
    fun enableFirebaseStorage(bucketUrl: String) {
        storageBucket = bucketUrl
    }

    /**
     * The function returns the value of the variable `storageBucket`.
     */
    fun getStorageBucket() = storageBucket

    /**
     * This function sets the basic configuration for Firebase using a builder pattern in Kotlin.
     *
     * @param config `config` is a lambda function that takes a `FirebaseOptions.Builder` object as input and returns a
     * modified `FirebaseOptions.Builder` object. The lambda function is defined using the syntax `() ->
     * FirebaseOptions.Builder?`, which means it takes no input parameters and returns an optional `FirebaseOptions.Builder
     */
    fun setBasicConfiguration(config: FirebaseOptions.Builder.() -> FirebaseOptions.Builder?) {
        basicConfiguration = config
    }

}