package com.kylix.plugins

import com.kylix.Firetor
import io.ktor.server.application.*

fun Application.configureFiretor() {
    install(Firetor) {
        adminKeyFileName = "firetor-admin-key.json"
        storageBucket = "firetor-ad044.appspot.com"
    }
}