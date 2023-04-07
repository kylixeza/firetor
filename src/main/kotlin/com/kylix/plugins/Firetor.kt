package com.kylix.plugins

import com.kylix.Firetor
import io.ktor.server.application.*

fun Application.configureFiretor() {
    install(Firetor) {
        setAdminKey("firetor-admin-key.json")
        enableFirebaseStorage("firetor-ad044.appspot.com")
    }
}