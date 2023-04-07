package com.kylix

import com.google.firebase.FirebaseOptions

object Configuration {

    var adminKeyFileName: String? = null
    var storageBucket: String? = null
    var basicConfiguration: FirebaseOptions.Builder.() -> FirebaseOptions.Builder? = { null }

}