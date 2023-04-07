package com.kylix.plugins

import com.kylix.FirebaseStorageImage.uploadImage
import com.kylix.ImageExtension
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

        post("/image") {
            val multipart = call.receiveMultipart()
            var url: String? = null

            try {
                multipart.forEachPart {part ->
                    if (part is PartData.FileItem) {
                        url = part.uploadImage()
                    }
                }
                call.respond(url ?: "No image uploaded")
            } catch (e: Exception) {
                call.respondText("Error: ${e.message}")
            }
        }

        post("/image-with-params") {
            val multipart = call.receiveMultipart()
            var url: String? = null

            try {
                multipart.forEachPart {part ->
                    if (part is PartData.FileItem) {
                        url = part.uploadImage(
                            path = "images",
                            fileExtension = ImageExtension.PNG,
                            preprocessing = {
                                it.compress(0.1f)
                            }
                        )
                    }
                }
                call.respond(url ?: "No image uploaded")
            } catch (e: Exception) {
                call.respondText("Error: ${e.message}")
            }
        }
    }
}
