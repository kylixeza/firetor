package com.kylix.plugins

import com.kylix.image.FirebaseStorageImage.uploadImage
import com.kylix.image.ImageExtension
import com.kylix.routes.postImage
import io.ktor.http.content.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.application.*
import io.ktor.server.request.*

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        this.postImage()
    }
}
